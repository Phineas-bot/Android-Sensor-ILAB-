package com.sensorscope.domain.usecase;

import android.content.Context;
import com.sensorscope.domain.repository.SensorRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class ExportSessionCsvUseCase_Factory implements Factory<ExportSessionCsvUseCase> {
  private final Provider<SensorRepository> repositoryProvider;

  private final Provider<Context> contextProvider;

  public ExportSessionCsvUseCase_Factory(Provider<SensorRepository> repositoryProvider,
      Provider<Context> contextProvider) {
    this.repositoryProvider = repositoryProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public ExportSessionCsvUseCase get() {
    return newInstance(repositoryProvider.get(), contextProvider.get());
  }

  public static ExportSessionCsvUseCase_Factory create(
      Provider<SensorRepository> repositoryProvider, Provider<Context> contextProvider) {
    return new ExportSessionCsvUseCase_Factory(repositoryProvider, contextProvider);
  }

  public static ExportSessionCsvUseCase newInstance(SensorRepository repository, Context context) {
    return new ExportSessionCsvUseCase(repository, context);
  }
}
