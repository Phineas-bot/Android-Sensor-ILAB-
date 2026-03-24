package com.sensorscope.data.local.dao;

import android.database.Cursor;
import androidx.annotation.NonNull;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.SharedSQLiteStatement;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import com.sensorscope.data.local.entity.SensorSessionEntity;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Long;
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
public final class SensorSessionDao_Impl implements SensorSessionDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<SensorSessionEntity> __insertionAdapterOfSensorSessionEntity;

  private final SharedSQLiteStatement __preparedStmtOfCloseSession;

  public SensorSessionDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfSensorSessionEntity = new EntityInsertionAdapter<SensorSessionEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `sensor_sessions` (`id`,`name`,`startedAtMillis`,`endedAtMillis`) VALUES (nullif(?, 0),?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SensorSessionEntity entity) {
        statement.bindLong(1, entity.getId());
        statement.bindString(2, entity.getName());
        statement.bindLong(3, entity.getStartedAtMillis());
        if (entity.getEndedAtMillis() == null) {
          statement.bindNull(4);
        } else {
          statement.bindLong(4, entity.getEndedAtMillis());
        }
      }
    };
    this.__preparedStmtOfCloseSession = new SharedSQLiteStatement(__db) {
      @Override
      @NonNull
      public String createQuery() {
        final String _query = "UPDATE sensor_sessions SET endedAtMillis = ? WHERE id = ?";
        return _query;
      }
    };
  }

  @Override
  public Object insertSession(final SensorSessionEntity session,
      final Continuation<? super Long> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Long>() {
      @Override
      @NonNull
      public Long call() throws Exception {
        __db.beginTransaction();
        try {
          final Long _result = __insertionAdapterOfSensorSessionEntity.insertAndReturnId(session);
          __db.setTransactionSuccessful();
          return _result;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Object closeSession(final long sessionId, final long endedAt,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        final SupportSQLiteStatement _stmt = __preparedStmtOfCloseSession.acquire();
        int _argIndex = 1;
        _stmt.bindLong(_argIndex, endedAt);
        _argIndex = 2;
        _stmt.bindLong(_argIndex, sessionId);
        try {
          __db.beginTransaction();
          try {
            _stmt.executeUpdateDelete();
            __db.setTransactionSuccessful();
            return Unit.INSTANCE;
          } finally {
            __db.endTransaction();
          }
        } finally {
          __preparedStmtOfCloseSession.release(_stmt);
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SensorSessionEntity>> observeSessions() {
    final String _sql = "SELECT * FROM sensor_sessions ORDER BY startedAtMillis DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sensor_sessions"}, new Callable<List<SensorSessionEntity>>() {
      @Override
      @NonNull
      public List<SensorSessionEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfName = CursorUtil.getColumnIndexOrThrow(_cursor, "name");
          final int _cursorIndexOfStartedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "startedAtMillis");
          final int _cursorIndexOfEndedAtMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "endedAtMillis");
          final List<SensorSessionEntity> _result = new ArrayList<SensorSessionEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SensorSessionEntity _item;
            final long _tmpId;
            _tmpId = _cursor.getLong(_cursorIndexOfId);
            final String _tmpName;
            _tmpName = _cursor.getString(_cursorIndexOfName);
            final long _tmpStartedAtMillis;
            _tmpStartedAtMillis = _cursor.getLong(_cursorIndexOfStartedAtMillis);
            final Long _tmpEndedAtMillis;
            if (_cursor.isNull(_cursorIndexOfEndedAtMillis)) {
              _tmpEndedAtMillis = null;
            } else {
              _tmpEndedAtMillis = _cursor.getLong(_cursorIndexOfEndedAtMillis);
            }
            _item = new SensorSessionEntity(_tmpId,_tmpName,_tmpStartedAtMillis,_tmpEndedAtMillis);
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
