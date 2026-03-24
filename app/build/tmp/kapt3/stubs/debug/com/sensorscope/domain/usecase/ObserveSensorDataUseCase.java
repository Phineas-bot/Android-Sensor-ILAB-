package com.sensorscope.domain.usecase;

import com.sensorscope.core.model.SensorData;
import com.sensorscope.core.model.SensorType;
import com.sensorscope.domain.repository.SensorRepository;
import javax.inject.Inject;
import kotlinx.coroutines.flow.Flow;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0017\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u00062\u0006\u0010\b\u001a\u00020\tH\u0086\u0002R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\n"}, d2 = {"Lcom/sensorscope/domain/usecase/ObserveSensorDataUseCase;", "", "repository", "Lcom/sensorscope/domain/repository/SensorRepository;", "(Lcom/sensorscope/domain/repository/SensorRepository;)V", "invoke", "Lkotlinx/coroutines/flow/Flow;", "Lcom/sensorscope/core/model/SensorData;", "type", "Lcom/sensorscope/core/model/SensorType;", "app_debug"})
public final class ObserveSensorDataUseCase {
    @org.jetbrains.annotations.NotNull()
    private final com.sensorscope.domain.repository.SensorRepository repository = null;
    
    @javax.inject.Inject()
    public ObserveSensorDataUseCase(@org.jetbrains.annotations.NotNull()
    com.sensorscope.domain.repository.SensorRepository repository) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.Flow<com.sensorscope.core.model.SensorData> invoke(@org.jetbrains.annotations.NotNull()
    com.sensorscope.core.model.SensorType type) {
        return null;
    }
}