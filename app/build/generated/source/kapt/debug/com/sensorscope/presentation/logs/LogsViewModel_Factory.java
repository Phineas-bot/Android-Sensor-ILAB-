package com.sensorscope.presentation.logs;

import com.sensorscope.domain.repository.SensorRepository;
import com.sensorscope.domain.usecase.ExportSessionCsvUseCase;
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
public final class LogsViewModel_Factory implements Factory<LogsViewModel> {
  private final Provider<SensorRepository> repositoryProvider;

  private final Provider<ExportSessionCsvUseCase> exportSessionCsvUseCaseProvider;

  public LogsViewModel_Factory(Provider<SensorRepository> repositoryProvider,
      Provider<ExportSessionCsvUseCase> exportSessionCsvUseCaseProvider) {
    this.repositoryProvider = repositoryProvider;
    this.exportSessionCsvUseCaseProvider = exportSessionCsvUseCaseProvider;
  }

  @Override
  public LogsViewModel get() {
    return newInstance(repositoryProvider.get(), exportSessionCsvUseCaseProvider.get());
  }

  public static LogsViewModel_Factory create(Provider<SensorRepository> repositoryProvider,
      Provider<ExportSessionCsvUseCase> exportSessionCsvUseCaseProvider) {
    return new LogsViewModel_Factory(repositoryProvider, exportSessionCsvUseCaseProvider);
  }

  public static LogsViewModel newInstance(SensorRepository repository,
      ExportSessionCsvUseCase exportSessionCsvUseCase) {
    return new LogsViewModel(repository, exportSessionCsvUseCase);
  }
}
