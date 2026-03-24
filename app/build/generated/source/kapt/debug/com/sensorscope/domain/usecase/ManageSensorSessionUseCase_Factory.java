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
public final class ManageSensorSessionUseCase_Factory implements Factory<ManageSensorSessionUseCase> {
  private final Provider<SensorRepository> repositoryProvider;

  public ManageSensorSessionUseCase_Factory(Provider<SensorRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ManageSensorSessionUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ManageSensorSessionUseCase_Factory create(
      Provider<SensorRepository> repositoryProvider) {
    return new ManageSensorSessionUseCase_Factory(repositoryProvider);
  }

  public static ManageSensorSessionUseCase newInstance(SensorRepository repository) {
    return new ManageSensorSessionUseCase(repository);
  }
}
