package com.sensorscope.di;

import com.sensorscope.data.local.SensorDatabase;
import com.sensorscope.data.local.dao.SensorSessionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideSensorSessionDaoFactory implements Factory<SensorSessionDao> {
  private final Provider<SensorDatabase> databaseProvider;

  public DatabaseModule_ProvideSensorSessionDaoFactory(Provider<SensorDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SensorSessionDao get() {
    return provideSensorSessionDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideSensorSessionDaoFactory create(
      Provider<SensorDatabase> databaseProvider) {
    return new DatabaseModule_ProvideSensorSessionDaoFactory(databaseProvider);
  }

  public static SensorSessionDao provideSensorSessionDao(SensorDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSensorSessionDao(database));
  }
}
