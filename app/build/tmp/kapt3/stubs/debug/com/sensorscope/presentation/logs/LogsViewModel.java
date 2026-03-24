package com.sensorscope.presentation.logs;

import androidx.lifecycle.ViewModel;
import com.sensorscope.domain.model.SensorSessionSummary;
import com.sensorscope.domain.repository.SensorRepository;
import com.sensorscope.domain.usecase.ExportSessionCsvUseCase;
import dagger.hilt.android.lifecycle.HiltViewModel;
import javax.inject.Inject;
import kotlinx.coroutines.flow.StateFlow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\u0006\u0010\u0013\u001a\u00020\u0014J\u000e\u0010\u0015\u001a\u00020\u00142\u0006\u0010\u0016\u001a\u00020\u0017R\u0016\u0010\u0007\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\n\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\t0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001d\u0010\u0011\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\f0\u000b0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0010\u00a8\u0006\u0018"}, d2 = {"Lcom/sensorscope/presentation/logs/LogsViewModel;", "Landroidx/lifecycle/ViewModel;", "repository", "Lcom/sensorscope/domain/repository/SensorRepository;", "exportSessionCsvUseCase", "Lcom/sensorscope/domain/usecase/ExportSessionCsvUseCase;", "(Lcom/sensorscope/domain/repository/SensorRepository;Lcom/sensorscope/domain/usecase/ExportSessionCsvUseCase;)V", "_exportedUri", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "_sessions", "", "Lcom/sensorscope/domain/model/SensorSessionSummary;", "exportedUri", "Lkotlinx/coroutines/flow/StateFlow;", "getExportedUri", "()Lkotlinx/coroutines/flow/StateFlow;", "sessions", "getSessions", "clearExportState", "", "exportSession", "sessionId", "", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class LogsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.sensorscope.domain.repository.SensorRepository repository = null;
    @org.jetbrains.annotations.NotNull()
    private final com.sensorscope.domain.usecase.ExportSessionCsvUseCase exportSessionCsvUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.sensorscope.domain.model.SensorSessionSummary>> _sessions = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sensorscope.domain.model.SensorSessionSummary>> sessions = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.String> _exportedUri = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.String> exportedUri = null;
    
    @javax.inject.Inject()
    public LogsViewModel(@org.jetbrains.annotations.NotNull()
    com.sensorscope.domain.repository.SensorRepository repository, @org.jetbrains.annotations.NotNull()
    com.sensorscope.domain.usecase.ExportSessionCsvUseCase exportSessionCsvUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.sensorscope.domain.model.SensorSessionSummary>> getSessions() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.String> getExportedUri() {
        return null;
    }
    
    public final void exportSession(long sessionId) {
    }
    
    public final void clearExportState() {
    }
}