package com.sensorscope.core.util;

import com.sensorscope.core.model.SensorData;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0004\n\u0002\u0010\u0016\n\u0000\n\u0002\u0010\u0014\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010J\u0006\u0010\u0011\u001a\u00020\u0003J\f\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00100\u0013R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0003X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0014"}, d2 = {"Lcom/sensorscope/core/util/SensorRingBuffer;", "", "capacity", "", "(I)V", "count", "start", "timestamps", "", "xValues", "", "yValues", "zValues", "add", "", "data", "Lcom/sensorscope/core/model/SensorData;", "size", "toList", "", "app_debug"})
public final class SensorRingBuffer {
    private final int capacity = 0;
    @org.jetbrains.annotations.NotNull()
    private final float[] xValues = null;
    @org.jetbrains.annotations.NotNull()
    private final float[] yValues = null;
    @org.jetbrains.annotations.NotNull()
    private final float[] zValues = null;
    @org.jetbrains.annotations.NotNull()
    private final long[] timestamps = null;
    private int count = 0;
    private int start = 0;
    
    public SensorRingBuffer(int capacity) {
        super();
    }
    
    public final void add(@org.jetbrains.annotations.NotNull()
    com.sensorscope.core.model.SensorData data) {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.sensorscope.core.model.SensorData> toList() {
        return null;
    }
    
    public final int size() {
        return 0;
    }
}