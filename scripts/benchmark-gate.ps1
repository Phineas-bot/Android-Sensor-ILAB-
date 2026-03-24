param(
    [string]$ProjectDir = (Resolve-Path ".").Path,
    [string]$SearchRoot = "",
    [string]$BaselineFile = "",
    [string]$ThresholdFile = "",
    [string]$ArtifactDir = "",
    [double]$DefaultMaxRegressionPercent = 10.0,
    [switch]$AllowMissingBaseline,
    [switch]$WriteBaseline
)

$ErrorActionPreference = "Stop"

if ([string]::IsNullOrWhiteSpace($SearchRoot)) {
    $SearchRoot = Join-Path $ProjectDir "app/build/outputs"
}
if ([string]::IsNullOrWhiteSpace($BaselineFile)) {
    $BaselineFile = Join-Path $ProjectDir "benchmarks/benchmark-baseline.json"
}
if ([string]::IsNullOrWhiteSpace($ThresholdFile)) {
    $ThresholdFile = Join-Path $ProjectDir "benchmarks/benchmark-thresholds.json"
}
if ([string]::IsNullOrWhiteSpace($ArtifactDir)) {
    $ArtifactDir = Join-Path $ProjectDir "build/benchmark-reports"
}

if ($env:BENCHMARK_MAX_REGRESSION_PERCENT) {
    $DefaultMaxRegressionPercent = [double]$env:BENCHMARK_MAX_REGRESSION_PERCENT
}

if (-not (Test-Path $ArtifactDir)) {
    New-Item -Path $ArtifactDir -ItemType Directory -Force | Out-Null
}

function Get-PercentRegression {
    param([double]$Current, [double]$Baseline)

    if ($Baseline -le 0) {
        return 0.0
    }
    return (($Current - $Baseline) / $Baseline) * 100.0
}

function Get-P95FromRuns {
    param([object[]]$Runs, [double]$Fallback)

    if ($null -eq $Runs -or $Runs.Count -eq 0) {
        return $Fallback
    }

    $sorted = $Runs | ForEach-Object { [double]$_ } | Sort-Object
    $index = [Math]::Ceiling(0.95 * $sorted.Count) - 1
    if ($index -lt 0) {
        $index = 0
    }
    if ($index -ge $sorted.Count) {
        $index = $sorted.Count - 1
    }
    return [double]$sorted[$index]
}

$benchmarkFiles = @()
if (Test-Path $SearchRoot) {
    $benchmarkFiles = Get-ChildItem -Path $SearchRoot -Recurse -Filter "benchmarkData.json" -File
}

if ($benchmarkFiles.Count -eq 0) {
    Write-Error "No benchmarkData.json files found under $SearchRoot. Run connected benchmark tests first."
}

$currentMetrics = @{}

foreach ($file in $benchmarkFiles) {
    $json = Get-Content -Path $file.FullName -Raw | ConvertFrom-Json -Depth 100
    if ($null -eq $json.benchmarks) {
        continue
    }

    foreach ($benchmark in $json.benchmarks) {
        $benchmarkKeyPrefix = if ($benchmark.className -and $benchmark.name) {
            "$($benchmark.className).$($benchmark.name)"
        }
        elseif ($benchmark.name) {
            [string]$benchmark.name
        }
        else {
            "unknown-benchmark"
        }

        $metricsProp = $benchmark.metrics.PSObject.Properties
        foreach ($metricProp in $metricsProp) {
            $metricName = $metricProp.Name
            $metricData = $metricProp.Value
            $medianNs = [double]$metricData.median
            $p95Ns = if ($metricData.PSObject.Properties.Name -contains "p95") {
                [double]$metricData.p95
            } else {
                Get-P95FromRuns -Runs @($metricData.runs) -Fallback $medianNs
            }

            $metricKey = "$benchmarkKeyPrefix::$metricName"
            if ($currentMetrics.ContainsKey($metricKey)) {
                $existing = $currentMetrics[$metricKey]
                $existing.medianNs = [Math]::Max([double]$existing.medianNs, $medianNs)
                $existing.p95Ns = [Math]::Max([double]$existing.p95Ns, $p95Ns)
            } else {
                $currentMetrics[$metricKey] = [ordered]@{
                    medianNs = $medianNs
                    p95Ns = $p95Ns
                    sources = @($file.FullName)
                }
            }
        }
    }
}

if ($WriteBaseline) {
    $baselineToWrite = [ordered]@{
        benchmarks = $currentMetrics
    }
    $baselineToWrite | ConvertTo-Json -Depth 100 | Set-Content -Path $BaselineFile -Encoding UTF8
    Write-Host "Baseline updated at $BaselineFile"
    exit 0
}

$thresholds = [ordered]@{
    defaultMaxRegressionPercent = $DefaultMaxRegressionPercent
    benchmarks = [ordered]@{}
}
if (Test-Path $ThresholdFile) {
    $thresholdJson = Get-Content -Path $ThresholdFile -Raw | ConvertFrom-Json -Depth 100
    foreach ($prop in $thresholdJson.PSObject.Properties) {
        switch ($prop.Name) {
            "defaultMaxRegressionPercent" {
                $thresholds.defaultMaxRegressionPercent = [double]$prop.Value
            }
            "benchmarks" {
                foreach ($benchmarkProp in $prop.Value.PSObject.Properties) {
                    $thresholds.benchmarks[$benchmarkProp.Name] = [double]$benchmarkProp.Value
                }
            }
        }
    }
}

$baseline = [ordered]@{ benchmarks = [ordered]@{} }
if (Test-Path $BaselineFile) {
    $baselineJson = Get-Content -Path $BaselineFile -Raw | ConvertFrom-Json -Depth 100
    foreach ($prop in $baselineJson.PSObject.Properties) {
        switch ($prop.Name) {
            "benchmarks" {
                foreach ($benchmarkProp in $prop.Value.PSObject.Properties) {
                    $baseline.benchmarks[$benchmarkProp.Name] = $benchmarkProp.Value
                }
            }
        }
    }
}

$rows = New-Object System.Collections.Generic.List[object]
$regressions = New-Object System.Collections.Generic.List[object]
$missingBaseline = New-Object System.Collections.Generic.List[string]

foreach ($metricKey in ($currentMetrics.Keys | Sort-Object)) {
    $current = $currentMetrics[$metricKey]
    $threshold = if ($thresholds.benchmarks.Contains($metricKey)) {
        [double]$thresholds.benchmarks[$metricKey]
    } else {
        [double]$thresholds.defaultMaxRegressionPercent
    }

    if (-not $baseline.benchmarks.Contains($metricKey)) {
        $missingBaseline.Add($metricKey) | Out-Null
        $rows.Add([ordered]@{
            benchmark = $metricKey
            medianNs = [double]$current.medianNs
            p95Ns = [double]$current.p95Ns
            baselineMedianNs = $null
            baselineP95Ns = $null
            medianRegressionPercent = $null
            p95RegressionPercent = $null
            thresholdPercent = $threshold
            status = "NO_BASELINE"
        }) | Out-Null
        continue
    }

    $base = $baseline.benchmarks[$metricKey]
    $baseMedian = [double]$base.medianNs
    $baseP95 = [double]$base.p95Ns

    $medianRegression = Get-PercentRegression -Current ([double]$current.medianNs) -Baseline $baseMedian
    $p95Regression = Get-PercentRegression -Current ([double]$current.p95Ns) -Baseline $baseP95

    $isRegression = ($medianRegression -gt $threshold) -or ($p95Regression -gt $threshold)
    if ($isRegression) {
        $regressions.Add([ordered]@{
            benchmark = $metricKey
            medianRegressionPercent = [Math]::Round($medianRegression, 3)
            p95RegressionPercent = [Math]::Round($p95Regression, 3)
            thresholdPercent = $threshold
        }) | Out-Null
    }

    $rows.Add([ordered]@{
        benchmark = $metricKey
        medianNs = [double]$current.medianNs
        p95Ns = [double]$current.p95Ns
        baselineMedianNs = $baseMedian
        baselineP95Ns = $baseP95
        medianRegressionPercent = [Math]::Round($medianRegression, 3)
        p95RegressionPercent = [Math]::Round($p95Regression, 3)
        thresholdPercent = $threshold
        status = if ($isRegression) { "REGRESSION" } else { "PASS" }
    }) | Out-Null
}

$report = [ordered]@{
    generatedAtUtc = (Get-Date).ToUniversalTime().ToString("o")
    searchRoot = $SearchRoot
    filesAnalyzed = @($benchmarkFiles | ForEach-Object { $_.FullName })
    thresholds = $thresholds
    summary = [ordered]@{
        benchmarkCount = $rows.Count
        regressionCount = $regressions.Count
        missingBaselineCount = $missingBaseline.Count
    }
    rows = $rows
    regressions = $regressions
    missingBaseline = $missingBaseline
}

$reportJsonPath = Join-Path $ArtifactDir "benchmark-report.json"
$reportMdPath = Join-Path $ArtifactDir "benchmark-report.md"

$report | ConvertTo-Json -Depth 100 | Set-Content -Path $reportJsonPath -Encoding UTF8

$md = New-Object System.Collections.Generic.List[string]
$md.Add("# Benchmark Gate Report") | Out-Null
$md.Add("") | Out-Null
$md.Add("- Generated (UTC): $($report.generatedAtUtc)") | Out-Null
$md.Add("- Benchmarks analyzed: $($report.summary.benchmarkCount)") | Out-Null
$md.Add("- Regressions: $($report.summary.regressionCount)") | Out-Null
$md.Add("- Missing baseline entries: $($report.summary.missingBaselineCount)") | Out-Null
$md.Add("") | Out-Null
$md.Add("| Benchmark | Median Ns | P95 Ns | Median Regression % | P95 Regression % | Threshold % | Status |") | Out-Null
$md.Add("|---|---:|---:|---:|---:|---:|---|") | Out-Null

foreach ($row in $rows) {
    $medianReg = "-"
    $p95Reg = "-"
    switch ($row.status) {
        "NO_BASELINE" {
            $medianReg = "-"
            $p95Reg = "-"
        }
        default {
            $medianReg = [string]$row.medianRegressionPercent
            $p95Reg = [string]$row.p95RegressionPercent
        }
    }
    $md.Add("| $($row.benchmark) | $([Math]::Round([double]$row.medianNs, 3)) | $([Math]::Round([double]$row.p95Ns, 3)) | $medianReg | $p95Reg | $($row.thresholdPercent) | $($row.status) |") | Out-Null
}

if ($missingBaseline.Count -gt 0) {
    $md.Add("") | Out-Null
    $md.Add("## Missing Baseline Entries") | Out-Null
    foreach ($item in $missingBaseline) {
        $md.Add("- $item") | Out-Null
    }
}

$md -join "`n" | Set-Content -Path $reportMdPath -Encoding UTF8

if ($regressions.Count -gt 0) {
    Write-Host "Benchmark regression gate failed. See $reportJsonPath and $reportMdPath"
    exit 1
}

if (($missingBaseline.Count -gt 0) -and (-not $AllowMissingBaseline)) {
    Write-Host "Benchmark gate failed due to missing baseline entries."
    exit 1
}

Write-Host "Benchmark gate passed. Report saved to $ArtifactDir"
exit 0
