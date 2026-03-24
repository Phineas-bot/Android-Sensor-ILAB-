package com.sensorscope.data.local;

import androidx.annotation.NonNull;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.RoomDatabase;
import androidx.room.RoomOpenHelper;
import androidx.room.migration.AutoMigrationSpec;
import androidx.room.migration.Migration;
import androidx.room.util.DBUtil;
import androidx.room.util.TableInfo;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;
import com.sensorscope.data.local.dao.SensorReadingDao;
import com.sensorscope.data.local.dao.SensorReadingDao_Impl;
import com.sensorscope.data.local.dao.SensorSessionDao;
import com.sensorscope.data.local.dao.SensorSessionDao_Impl;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SensorDatabase_Impl extends SensorDatabase {
  private volatile SensorSessionDao _sensorSessionDao;

  private volatile SensorReadingDao _sensorReadingDao;

  @Override
  @NonNull
  protected SupportSQLiteOpenHelper createOpenHelper(@NonNull final DatabaseConfiguration config) {
    final SupportSQLiteOpenHelper.Callback _openCallback = new RoomOpenHelper(config, new RoomOpenHelper.Delegate(1) {
      @Override
      public void createAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS `sensor_sessions` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL, `startedAtMillis` INTEGER NOT NULL, `endedAtMillis` INTEGER)");
        db.execSQL("CREATE TABLE IF NOT EXISTS `sensor_readings` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `sessionId` INTEGER NOT NULL, `sensorType` TEXT NOT NULL, `x` REAL NOT NULL, `y` REAL NOT NULL, `z` REAL NOT NULL, `timestampNanos` INTEGER NOT NULL, `recordedAtMillis` INTEGER NOT NULL, FOREIGN KEY(`sessionId`) REFERENCES `sensor_sessions`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_sensor_readings_sessionId` ON `sensor_readings` (`sessionId`)");
        db.execSQL("CREATE INDEX IF NOT EXISTS `index_sensor_readings_sensorType` ON `sensor_readings` (`sensorType`)");
        db.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)");
        db.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '96074562fbc5e831852d68958d5e847a')");
      }

      @Override
      public void dropAllTables(@NonNull final SupportSQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS `sensor_sessions`");
        db.execSQL("DROP TABLE IF EXISTS `sensor_readings`");
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onDestructiveMigration(db);
          }
        }
      }

      @Override
      public void onCreate(@NonNull final SupportSQLiteDatabase db) {
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onCreate(db);
          }
        }
      }

      @Override
      public void onOpen(@NonNull final SupportSQLiteDatabase db) {
        mDatabase = db;
        db.execSQL("PRAGMA foreign_keys = ON");
        internalInitInvalidationTracker(db);
        final List<? extends RoomDatabase.Callback> _callbacks = mCallbacks;
        if (_callbacks != null) {
          for (RoomDatabase.Callback _callback : _callbacks) {
            _callback.onOpen(db);
          }
        }
      }

      @Override
      public void onPreMigrate(@NonNull final SupportSQLiteDatabase db) {
        DBUtil.dropFtsSyncTriggers(db);
      }

      @Override
      public void onPostMigrate(@NonNull final SupportSQLiteDatabase db) {
      }

      @Override
      @NonNull
      public RoomOpenHelper.ValidationResult onValidateSchema(
          @NonNull final SupportSQLiteDatabase db) {
        final HashMap<String, TableInfo.Column> _columnsSensorSessions = new HashMap<String, TableInfo.Column>(4);
        _columnsSensorSessions.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSensorSessions.put("name", new TableInfo.Column("name", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSensorSessions.put("startedAtMillis", new TableInfo.Column("startedAtMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSensorSessions.put("endedAtMillis", new TableInfo.Column("endedAtMillis", "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSensorSessions = new HashSet<TableInfo.ForeignKey>(0);
        final HashSet<TableInfo.Index> _indicesSensorSessions = new HashSet<TableInfo.Index>(0);
        final TableInfo _infoSensorSessions = new TableInfo("sensor_sessions", _columnsSensorSessions, _foreignKeysSensorSessions, _indicesSensorSessions);
        final TableInfo _existingSensorSessions = TableInfo.read(db, "sensor_sessions");
        if (!_infoSensorSessions.equals(_existingSensorSessions)) {
          return new RoomOpenHelper.ValidationResult(false, "sensor_sessions(com.sensorscope.data.local.entity.SensorSessionEntity).\n"
                  + " Expected:\n" + _infoSensorSessions + "\n"
                  + " Found:\n" + _existingSensorSessions);
        }
        final HashMap<String, TableInfo.Column> _columnsSensorReadings = new HashMap<String, TableInfo.Column>(8);
        _columnsSensorReadings.put("id", new TableInfo.Column("id", "INTEGER", true, 1, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSensorReadings.put("sessionId", new TableInfo.Column("sessionId", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSensorReadings.put("sensorType", new TableInfo.Column("sensorType", "TEXT", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSensorReadings.put("x", new TableInfo.Column("x", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSensorReadings.put("y", new TableInfo.Column("y", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSensorReadings.put("z", new TableInfo.Column("z", "REAL", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSensorReadings.put("timestampNanos", new TableInfo.Column("timestampNanos", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        _columnsSensorReadings.put("recordedAtMillis", new TableInfo.Column("recordedAtMillis", "INTEGER", true, 0, null, TableInfo.CREATED_FROM_ENTITY));
        final HashSet<TableInfo.ForeignKey> _foreignKeysSensorReadings = new HashSet<TableInfo.ForeignKey>(1);
        _foreignKeysSensorReadings.add(new TableInfo.ForeignKey("sensor_sessions", "CASCADE", "NO ACTION", Arrays.asList("sessionId"), Arrays.asList("id")));
        final HashSet<TableInfo.Index> _indicesSensorReadings = new HashSet<TableInfo.Index>(2);
        _indicesSensorReadings.add(new TableInfo.Index("index_sensor_readings_sessionId", false, Arrays.asList("sessionId"), Arrays.asList("ASC")));
        _indicesSensorReadings.add(new TableInfo.Index("index_sensor_readings_sensorType", false, Arrays.asList("sensorType"), Arrays.asList("ASC")));
        final TableInfo _infoSensorReadings = new TableInfo("sensor_readings", _columnsSensorReadings, _foreignKeysSensorReadings, _indicesSensorReadings);
        final TableInfo _existingSensorReadings = TableInfo.read(db, "sensor_readings");
        if (!_infoSensorReadings.equals(_existingSensorReadings)) {
          return new RoomOpenHelper.ValidationResult(false, "sensor_readings(com.sensorscope.data.local.entity.SensorReadingEntity).\n"
                  + " Expected:\n" + _infoSensorReadings + "\n"
                  + " Found:\n" + _existingSensorReadings);
        }
        return new RoomOpenHelper.ValidationResult(true, null);
      }
    }, "96074562fbc5e831852d68958d5e847a", "513febcb1efdaddc1033625790c2046c");
    final SupportSQLiteOpenHelper.Configuration _sqliteConfig = SupportSQLiteOpenHelper.Configuration.builder(config.context).name(config.name).callback(_openCallback).build();
    final SupportSQLiteOpenHelper _helper = config.sqliteOpenHelperFactory.create(_sqliteConfig);
    return _helper;
  }

  @Override
  @NonNull
  protected InvalidationTracker createInvalidationTracker() {
    final HashMap<String, String> _shadowTablesMap = new HashMap<String, String>(0);
    final HashMap<String, Set<String>> _viewTables = new HashMap<String, Set<String>>(0);
    return new InvalidationTracker(this, _shadowTablesMap, _viewTables, "sensor_sessions","sensor_readings");
  }

  @Override
  public void clearAllTables() {
    super.assertNotMainThread();
    final SupportSQLiteDatabase _db = super.getOpenHelper().getWritableDatabase();
    final boolean _supportsDeferForeignKeys = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP;
    try {
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = FALSE");
      }
      super.beginTransaction();
      if (_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA defer_foreign_keys = TRUE");
      }
      _db.execSQL("DELETE FROM `sensor_sessions`");
      _db.execSQL("DELETE FROM `sensor_readings`");
      super.setTransactionSuccessful();
    } finally {
      super.endTransaction();
      if (!_supportsDeferForeignKeys) {
        _db.execSQL("PRAGMA foreign_keys = TRUE");
      }
      _db.query("PRAGMA wal_checkpoint(FULL)").close();
      if (!_db.inTransaction()) {
        _db.execSQL("VACUUM");
      }
    }
  }

  @Override
  @NonNull
  protected Map<Class<?>, List<Class<?>>> getRequiredTypeConverters() {
    final HashMap<Class<?>, List<Class<?>>> _typeConvertersMap = new HashMap<Class<?>, List<Class<?>>>();
    _typeConvertersMap.put(SensorSessionDao.class, SensorSessionDao_Impl.getRequiredConverters());
    _typeConvertersMap.put(SensorReadingDao.class, SensorReadingDao_Impl.getRequiredConverters());
    return _typeConvertersMap;
  }

  @Override
  @NonNull
  public Set<Class<? extends AutoMigrationSpec>> getRequiredAutoMigrationSpecs() {
    final HashSet<Class<? extends AutoMigrationSpec>> _autoMigrationSpecsSet = new HashSet<Class<? extends AutoMigrationSpec>>();
    return _autoMigrationSpecsSet;
  }

  @Override
  @NonNull
  public List<Migration> getAutoMigrations(
      @NonNull final Map<Class<? extends AutoMigrationSpec>, AutoMigrationSpec> autoMigrationSpecs) {
    final List<Migration> _autoMigrations = new ArrayList<Migration>();
    return _autoMigrations;
  }

  @Override
  public SensorSessionDao sensorSessionDao() {
    if (_sensorSessionDao != null) {
      return _sensorSessionDao;
    } else {
      synchronized(this) {
        if(_sensorSessionDao == null) {
          _sensorSessionDao = new SensorSessionDao_Impl(this);
        }
        return _sensorSessionDao;
      }
    }
  }

  @Override
  public SensorReadingDao sensorReadingDao() {
    if (_sensorReadingDao != null) {
      return _sensorReadingDao;
    } else {
      synchronized(this) {
        if(_sensorReadingDao == null) {
          _sensorReadingDao = new SensorReadingDao_Impl(this);
        }
        return _sensorReadingDao;
      }
    }
  }
}
