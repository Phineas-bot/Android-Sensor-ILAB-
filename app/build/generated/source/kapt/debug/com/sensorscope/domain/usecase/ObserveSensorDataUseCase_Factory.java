package com.sensorscope.domain.usecase;

import com.sensorscope.domain.repository.SensorRepository;
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
public final class ObserveSensorDataUseCase_Factory implements Factory<ObserveSensorDataUseCase> {
  private final Provider<SensorRepository> repositoryProvider;

  public ObserveSensorDataUseCase_Factory(Provider<SensorRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ObserveSensorDataUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ObserveSensorDataUseCase_Factory create(
      Provider<SensorRepository> repositoryProvider) {
    return new ObserveSensorDataUseCase_Factory(repositoryProvider);
  }

  public static ObserveSensorDataUseCase newInstance(SensorRepository repository) {
    return new ObserveSensorDataUseCase(repository);
  }
}
