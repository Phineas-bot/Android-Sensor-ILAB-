package com.sensorscope.data.local.dao;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.sensorscope.data.local.entity.SensorReadingEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SensorReadingDao_Impl implements SensorReadingDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SensorReadingEntity> __insertionAdapterOfSensorReadingEntity;

  public SensorReadingDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSensorReadingEntity = new EntityInsertionAdapter<SensorReadingEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `sensor_readings` (`id`,`sessionId`,`sensorType`,`x`,`y`,`z`,`timestampNanos`,`recordedAtMillis`) VALUES (nullif(?, 0),?,?,?,?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SensorReadingEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindLong(2, entity.getSessionId());
        statement.bindString(3, entity.getSensorType());
        statement.bindDouble(4, entity.getX());
        statement.bindDouble(5, entity.getY());
        statement.bindDouble(6, entity.getZ());
        statement.bindLong(7, entity.getTimestampNanos());
        statement.bindLong(8, entity.getRecordedAtMillis());
      }
    };
  }

  @Override
  public Object insertReading(final SensorReadingEntity reading,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfSensorReadingEntity.insert(reading);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object getReadingsBySession(final long sessionId,
      final Continuation<? super List<SensorReadingEntity>> $completion) {
    final String _sql = "SELECT * FROM sensor_readings WHERE sessionId = ? ORDER BY recordedAtMillis ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<List<SensorReadingEntity>>() {
      @Override
      @NonNull
      public List<SensorReadingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfSensorType = CursorUtil.getColumnIndexOrThrow(_cursor, "sensorType");
          final int _cursorIndexOfX = CursorUtil.getColumnIndexOrThrow(_cursor, "x");
          final int _cursorIndexOfY = CursorUtil.getColumnIndexOrThrow(_cursor, "y");
          final int _cursorIndexOfZ = CursorUtil.getColumnIndexOrThrow(_cursor, "z");
          final int _cursorIndexOfTimestampNanos = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampNanos");
          final int _cursorIndexOfRecordedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "recordedAtMillis");
          final List<SensorReadingEntity> _result = new ArrayList<SensorReadingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SensorReadingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final String _tmpSensorType;
            _tmpSensorType = _cursor.getString(_cursorIndexOfSensorType);
            final float _tmpX;
            _tmpX = _cursor.getFloat(_cursorIndexOfX);
            final float _tmpY;
            _tmpY = _cursor.getFloat(_cursorIndexOfY);
            final float _tmpZ;
            _tmpZ = _cursor.getFloat(_cursorIndexOfZ);
            final long _tmpTimestampNanos;
            _tmpTimestampNanos = _cursor.getLong(_cursorIndexOfTimestampNanos);
            final long _tmpRecordedAtMillis;
            _tmpRecordedAtMillis = _cursor.getLong(_cursorIndexOfRecordedAtMillis);
            _item = new SensorReadingEntity(_tmpId,_tmpSessionId,_tmpSensorType,_tmpX,_tmpY,_tmpZ,_tmpTimestampNanos,_tmpRecordedAtMillis);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SensorReadingEntity>> observeReadingsBySession(final long sessionId) {
    final String _sql = "SELECT * FROM sensor_readings WHERE sessionId = ? ORDER BY recordedAtMillis DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindLong(_argIndex, sessionId);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sensor_readings"}, new Callable<List<SensorReadingEntity>>() {
      @Override
      @NonNull
      public List<SensorReadingEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfSessionId = CursorUtil.getColumnIndexOrThrow(_cursor, "sessionId");
          final int _cursorIndexOfSensorType = CursorUtil.getColumnIndexOrThrow(_cursor, "sensorType");
          final int _cursorIndexOfX = CursorUtil.getColumnIndexOrThrow(_cursor, "x");
          final int _cursorIndexOfY = CursorUtil.getColumnIndexOrThrow(_cursor, "y");
          final int _cursorIndexOfZ = CursorUtil.getColumnIndexOrThrow(_cursor, "z");
          final int _cursorIndexOfTimestampNanos = CursorUtil.getColumnIndexOrThrow(_cursor, "timestampNanos");
          final int _cursorIndexOfRecordedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "recordedAtMillis");
          final List<SensorReadingEntity> _result = new ArrayList<SensorReadingEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SensorReadingEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final long _tmpSessionId;
            _tmpSessionId = _cursor.getLong(_cursorIndexOfSessionId);
            final String _tmpSensorType;
            _tmpSensorType = _cursor.getString(_cursorIndexOfSensorType);
            final float _tmpX;
            _tmpX = _cursor.getFloat(_cursorIndexOfX);
            final float _tmpY;
            _tmpY = _cursor.getFloat(_cursorIndexOfY);
            final float _tmpZ;
            _tmpZ = _cursor.getFloat(_cursorIndexOfZ);
            final long _tmpTimestampNanos;
            _tmpTimestampNanos = _cursor.getLong(_cursorIndexOfTimestampNanos);
            final long _tmpRecordedAtMillis;
            _tmpRecordedAtMillis = _cursor.getLong(_cursorIndexOfRecordedAtMillis);
            _item = new SensorReadingEntity(_tmpId,_tmpSessionId,_tmpSensorType,_tmpX,_tmpY,_tmpZ,_tmpTimestampNanos,_tmpRecordedAtMillis);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
