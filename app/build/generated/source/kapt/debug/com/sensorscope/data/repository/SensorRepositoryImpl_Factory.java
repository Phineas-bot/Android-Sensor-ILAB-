package com.sensorscope.data.repository;

import com.sensorscope.core.sensor.SensorManagerHelper;
import com.sensorscope.data.local.dao.SensorReadingDao;
import com.sensorscope.data.local.dao.SensorSessionDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class SensorRepositoryImpl_Factory implements Factory<SensorRepositoryImpl> {
  private final Provider<SensorManagerHelper> sensorManagerHelperProvider;

  private final Provider<SensorSessionDao> sessionDaoProvider;

  private final Provider<SensorReadingDao> readingDaoProvider;

  public SensorRepositoryImpl_Factory(Provider<SensorManagerHelper> sensorManagerHelperProvider,
      Provider<SensorSessionDao> sessionDaoProvider,
      Provider<SensorReadingDao> readingDaoProvider) {
    this.sensorManagerHelperProvider = sensorManagerHelperProvider;
    this.sessionDaoProvider = sessionDaoProvider;
    this.readingDaoProvider = readingDaoProvider;
  }

  @Override
  public SensorRepositoryImpl get() {
    return newInstance(sensorManagerHelperProvider.get(), sessionDaoProvider.get(), readingDaoProvider.get());
  }

  public static SensorRepositoryImpl_Factory create(
      Provider<SensorManagerHelper> sensorManagerHelperProvider,
      Provider<SensorSessionDao> sessionDaoProvider,
      Provider<SensorReadingDao> readingDaoProvider) {
    return new SensorRepositoryImpl_Factory(sensorManagerHelperProvider, sessionDaoProvider, readingDaoProvider);
  }

  public static SensorRepositoryImpl newInstance(SensorManagerHelper sensorManagerHelper,
      SensorSessionDao sessionDao, SensorReadingDao readingDao) {
    return new SensorRepositoryImpl(sensorManagerHelper, sessionDao, readingDao);
  }
}
