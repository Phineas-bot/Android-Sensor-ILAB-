package com.sensorscope.di;

import com.sensorscope.data.local.SensorDatabase;
import com.sensorscope.data.local.dao.SensorReadingDao;
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
public final class DatabaseModule_ProvideSensorReadingDaoFactory implements Factory<SensorReadingDao> {
  private final Provider<SensorDatabase> databaseProvider;

  public DatabaseModule_ProvideSensorReadingDaoFactory(Provider<SensorDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public SensorReadingDao get() {
    return provideSensorReadingDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvideSensorReadingDaoFactory create(
      Provider<SensorDatabase> databaseProvider) {
    return new DatabaseModule_ProvideSensorReadingDaoFactory(databaseProvider);
  }

  public static SensorReadingDao provideSensorReadingDao(SensorDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSensorReadingDao(database));
  }
}
