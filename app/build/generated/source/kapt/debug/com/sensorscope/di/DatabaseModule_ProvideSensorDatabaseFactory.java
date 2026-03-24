package com.sensorscope.di;

import android.content.Context;
import com.sensorscope.data.local.SensorDatabase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DatabaseModule_ProvideSensorDatabaseFactory implements Factory<SensorDatabase> {
  private final Provider<Context> contextProvider;

  public DatabaseModule_ProvideSensorDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public SensorDatabase get() {
    return provideSensorDatabase(contextProvider.get());
  }

  public static DatabaseModule_ProvideSensorDatabaseFactory create(
      Provider<Context> contextProvider) {
    return new DatabaseModule_ProvideSensorDatabaseFactory(contextProvider);
  }

  public static SensorDatabase provideSensorDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideSensorDatabase(context));
  }
}
