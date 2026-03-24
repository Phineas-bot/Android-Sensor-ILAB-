package com.sensorscope.presentation.sensor;

import com.sensorscope.domain.usecase.ManageSensorSessionUseCase;
import com.sensorscope.domain.usecase.ObserveSensorDataUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class SensorViewModel_Factory implements Factory<SensorViewModel> {
  private final Provider<ObserveSensorDataUseCase> observeSensorDataUseCaseProvider;

  private final Provider<ManageSensorSessionUseCase> manageSensorSessionUseCaseProvider;

  public SensorViewModel_Factory(
      Provider<ObserveSensorDataUseCase> observeSensorDataUseCaseProvider,
      Provider<ManageSensorSessionUseCase> manageSensorSessionUseCaseProvider) {
    this.observeSensorDataUseCaseProvider = observeSensorDataUseCaseProvider;
    this.manageSensorSessionUseCaseProvider = manageSensorSessionUseCaseProvider;
  }

  @Override
  public SensorViewModel get() {
    return newInstance(observeSensorDataUseCaseProvider.get(), manageSensorSessionUseCaseProvider.get());
  }

  public static SensorViewModel_Factory create(
      Provider<ObserveSensorDataUseCase> observeSensorDataUseCaseProvider,
      Provider<ManageSensorSessionUseCase> manageSensorSessionUseCaseProvider) {
    return new SensorViewModel_Factory(observeSensorDataUseCaseProvider, manageSensorSessionUseCaseProvider);
  }

  public static SensorViewModel newInstance(ObserveSensorDataUseCase observeSensorDataUseCase,
      ManageSensorSessionUseCase manageSensorSessionUseCase) {
    return new SensorViewModel(observeSensorDataUseCase, manageSensorSessionUseCase);
  }
}
