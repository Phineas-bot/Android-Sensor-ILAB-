package com.sensorscope

import android.os.Debug
import android.os.SystemClock
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PerformanceBaselineInstrumentationTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun startupBaselineWithinFiveSeconds() {
        val start = SystemClock.elapsedRealtime()

        composeRule.onNodeWithText("Dashboard").assertIsDisplayed()
        val elapsedMs = SystemClock.elapsedRealtime() - start

        assertTrue("Startup took ${elapsedMs}ms, expected < 5000ms", elapsedMs < 5000L)
    }

    @Test
    fun baselinePssMemoryBelowThresholdAfterNavigationWarmup() {
        composeRule.onNodeWithText("Labs").performClick()
        composeRule.onNodeWithText("Logs").performClick()
        composeRule.onNodeWithText("Dashboard").performClick()
        composeRule.waitForIdle()

        val pssKb = Debug.getPss()

        assertTrue("PSS memory ${pssKb}KB exceeds threshold", pssKb in 1..350_000)
    }
}
