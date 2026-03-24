package com.sensorscope.presentation.components;

import android.graphics.Color;
import androidx.compose.runtime.Composable;
import androidx.compose.ui.Modifier;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.sensorscope.core.model.SensorData;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00006\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\u001a \u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0002\u0010\u0005\u001a\u00020\u0006H\u0007\u001a \u0010\u0007\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000bH\u0002\u001a\b\u0010\r\u001a\u00020\u000eH\u0002\u001a\u0018\u0010\u000f\u001a\u00020\u00012\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\u0010\u001a\u00020\u0011H\u0002\u00a8\u0006\u0012"}, d2 = {"SensorLineChart", "", "points", "", "Lcom/sensorscope/core/model/SensorData;", "modifier", "Landroidx/compose/ui/Modifier;", "appendPoint", "dataSet", "Lcom/github/mikephil/charting/data/LineDataSet;", "x", "", "y", "createAxisSets", "Lcom/sensorscope/presentation/components/AxisSets;", "trimToWindow", "maxWindowSize", "", "app_debug"})
public final class SensorLineChartKt {
    
    private static final com.sensorscope.presentation.components.AxisSets createAxisSets() {
        return null;
    }
    
    private static final void appendPoint(com.github.mikephil.charting.data.LineDataSet dataSet, float x, float y) {
    }
    
    private static final void trimToWindow(com.github.mikephil.charting.data.LineDataSet dataSet, int maxWindowSize) {
    }
    
    @androidx.compose.runtime.Composable()
    public static final void SensorLineChart(@org.jetbrains.annotations.NotNull()
    java.util.List<com.sensorscope.core.model.SensorData> points, @org.jetbrains.annotations.NotNull()
    androidx.compose.ui.Modifier modifier) {
    }
}