package com.sensorscope.presentation.sensor;

import androidx.lifecycle.ViewModel;
import com.sensorscope.core.model.SensorData;
import com.sensorscope.core.model.SensorType;
import com.sensorscope.core.util.SensorRingBuffer;
import com.sensorscope.domain.lab.SensorLab;
import com.sensorscope.domain.lab.SensorLabEngine;
import com.sensorscope.domain.usecase.ManageSensorSessionUseCase;
import com.sensorscope.domain.usecase.ObserveSensorDataUseCase;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000x\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u000b\n\u0002\b\u0006\b\u0007\u0018\u0000 02\u00020\u0001:\u00010B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u001e\u001a\u00020\u001fH\u0014J\u0018\u0010 \u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\u000f2\u0006\u0010\"\u001a\u00020#H\u0002J\u0010\u0010$\u001a\u00020\u001f2\u0006\u0010%\u001a\u00020&H\u0002J\u000e\u0010\'\u001a\u00020\u001f2\u0006\u0010(\u001a\u00020\u0015J\u000e\u0010)\u001a\u00020\u001f2\u0006\u0010*\u001a\u00020+J\u0006\u0010,\u001a\u00020\u001fJ\u0018\u0010-\u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\u000f2\u0006\u0010%\u001a\u00020&H\u0002J\u0006\u0010.\u001a\u00020\u001fJ\u0018\u0010/\u001a\u00020\u001f2\u0006\u0010!\u001a\u00020\u000f2\u0006\u0010\"\u001a\u00020#H\u0002R\u001a\u0010\u0007\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\f0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\r\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00100\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\u000e\u0012\u0004\u0012\u00020\u000f\u0012\u0004\u0012\u00020\u00130\u0012X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0018\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\n0\t0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001a\u0010\u001bR\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\f0\u0019\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001d\u0010\u001b\u00a8\u00061"}, d2 = {"Lcom/sensorscope/presentation/sensor/SensorViewModel;", "Landroidx/lifecycle/ViewModel;", "observeSensorDataUseCase", "Lcom/sensorscope/domain/usecase/ObserveSensorDataUseCase;", "manageSensorSessionUseCase", "Lcom/sensorscope/domain/usecase/ManageSensorSessionUseCase;", "(Lcom/sensorscope/domain/usecase/ObserveSensorDataUseCase;Lcom/sensorscope/domain/usecase/ManageSensorSessionUseCase;)V", "_labs", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "Lcom/sensorscope/domain/lab/SensorLab;", "_uiState", "Lcom/sensorscope/presentation/sensor/SensorUiState;", "activeJobs", "", "Lcom/sensorscope/core/model/SensorType;", "Lkotlinx/coroutines/Job;", "chartBuffers", "", "Lcom/sensorscope/core/util/SensorRingBuffer;", "collectionScope", "Lcom/sensorscope/presentation/sensor/SensorCollectionScope;", "labEngine", "Lcom/sensorscope/domain/lab/SensorLabEngine;", "labs", "Lkotlinx/coroutines/flow/StateFlow;", "getLabs", "()Lkotlinx/coroutines/flow/StateFlow;", "uiState", "getUiState", "onCleared", "", "onSensorData", "type", "data", "Lcom/sensorscope/core/model/SensorData;", "reconcileCollectors", "sessionId", "", "setCollectionScope", "scope", "setSamplingRateFast", "enabled", "", "startListening", "startSensorCollection", "stopListening", "updateLabs", "Companion", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class SensorViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.sensorscope.domain.usecase.ObserveSensorDataUseCase observeSensorDataUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sensorscope.domain.usecase.ManageSensorSessionUseCase manageSensorSessionUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.sensorscope.presentation.sensor.SensorUiState> _uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.sensorscope.presentation.sensor.SensorUiState> uiState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.sensorscope.domain.lab.SensorLab>> _labs = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sensorscope.domain.lab.SensorLab>> labs = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<com.sensorscope.core.model.SensorType, com.sensorscope.core.util.SensorRingBuffer> chartBuffers = null;
    @org.jetbrains.annotations.NotNull()
    private final java.util.Map<com.sensorscope.core.model.SensorType, kotlinx.coroutines.Job> activeJobs = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sensorscope.domain.lab.SensorLabEngine labEngine = null;
    @org.jetbrains.annotations.NotNull()
    private com.sensorscope.presentation.sensor.SensorCollectionScope collectionScope;
    private static final int CHART_BUFFER_CAPACITY = 180;
    @org.jetbrains.annotations.NotNull()
    public static final com.sensorscope.presentation.sensor.SensorViewModel.Companion Companion = null;
    
    @javax.inject.Inject()
    public SensorViewModel(@org.jetbrains.annotations.NotNull()
    com.sensorscope.domain.usecase.ObserveSensorDataUseCase observeSensorDataUseCase, @org.jetbrains.annotations.NotNull()
    com.sensorscope.domain.usecase.ManageSensorSessionUseCase manageSensorSessionUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.sensorscope.presentation.sensor.SensorUiState> getUiState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sensorscope.domain.lab.SensorLab>> getLabs() {
        return null;
    }
    
    public final void startListening() {
    }
    
    public final void stopListening() {
    }
    
    private final void startSensorCollection(com.sensorscope.core.model.SensorType type, long sessionId) {
    }
    
    public final void setCollectionScope(@org.jetbrains.annotations.NotNull()
    com.sensorscope.presentation.sensor.SensorCollectionScope scope) {
    }
    
    private final void reconcileCollectors(long sessionId) {
    }
    
    private final void onSensorData(com.sensorscope.core.model.SensorType type, com.sensorscope.core.model.SensorData data) {
    }
    
    private final void updateLabs(com.sensorscope.core.model.SensorType type, com.sensorscope.core.model.SensorData data) {
    }
    
    public final void setSamplingRateFast(boolean enabled) {
    }
    
    @java.lang.Override()
    protected void onCleared() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0005"}, d2 = {"Lcom/sensorscope/presentation/sensor/SensorViewModel$Companion;", "", "()V", "CHART_BUFFER_CAPACITY", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}