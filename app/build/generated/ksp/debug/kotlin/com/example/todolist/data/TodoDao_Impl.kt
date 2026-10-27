package com.example.todolist.`data`

import androidx.room.EntityDeleteOrUpdateAdapter
import androidx.room.EntityInsertAdapter
import androidx.room.RoomDatabase
import androidx.room.coroutines.createFlow
import androidx.room.util.getColumnIndexOrThrow
import androidx.room.util.performSuspending
import androidx.sqlite.SQLiteStatement
import com.example.todolist.model.Todo
import java.util.Date
import javax.`annotation`.processing.Generated
import kotlin.Boolean
import kotlin.Int
import kotlin.Long
import kotlin.String
import kotlin.Suppress
import kotlin.Unit
import kotlin.collections.List
import kotlin.collections.MutableList
import kotlin.collections.mutableListOf
import kotlin.reflect.KClass
import kotlinx.coroutines.flow.Flow

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class TodoDao_Impl(
  __db: RoomDatabase,
) : TodoDao {
  private val __db: RoomDatabase

  private val __insertAdapterOfTodo: EntityInsertAdapter<Todo>

  private val __dateConverter: DateConverter = DateConverter()

  private val __deleteAdapterOfTodo: EntityDeleteOrUpdateAdapter<Todo>

  private val __updateAdapterOfTodo: EntityDeleteOrUpdateAdapter<Todo>
  init {
    this.__db = __db
    this.__insertAdapterOfTodo = object : EntityInsertAdapter<Todo>() {
      protected override fun createQuery(): String = "INSERT OR IGNORE INTO `todos` (`id`,`title`,`completed`,`plannedDate`,`plannedStartTime`,`plannedEndTime`) VALUES (nullif(?, 0),?,?,?,?,?)"

      protected override fun bind(statement: SQLiteStatement, entity: Todo) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.title)
        val _tmp: Int = if (entity.completed) 1 else 0
        statement.bindLong(3, _tmp.toLong())
        val _tmpPlannedDate: Date? = entity.plannedDate
        val _tmp_1: Long? = __dateConverter.dateToTimestamp(_tmpPlannedDate)
        if (_tmp_1 == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmp_1)
        }
        val _tmpPlannedStartTime: Date? = entity.plannedStartTime
        val _tmp_2: Long? = __dateConverter.dateToTimestamp(_tmpPlannedStartTime)
        if (_tmp_2 == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmp_2)
        }
        val _tmpPlannedEndTime: Date? = entity.plannedEndTime
        val _tmp_3: Long? = __dateConverter.dateToTimestamp(_tmpPlannedEndTime)
        if (_tmp_3 == null) {
          statement.bindNull(6)
        } else {
          statement.bindLong(6, _tmp_3)
        }
      }
    }
    this.__deleteAdapterOfTodo = object : EntityDeleteOrUpdateAdapter<Todo>() {
      protected override fun createQuery(): String = "DELETE FROM `todos` WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Todo) {
        statement.bindLong(1, entity.id.toLong())
      }
    }
    this.__updateAdapterOfTodo = object : EntityDeleteOrUpdateAdapter<Todo>() {
      protected override fun createQuery(): String = "UPDATE OR ABORT `todos` SET `id` = ?,`title` = ?,`completed` = ?,`plannedDate` = ?,`plannedStartTime` = ?,`plannedEndTime` = ? WHERE `id` = ?"

      protected override fun bind(statement: SQLiteStatement, entity: Todo) {
        statement.bindLong(1, entity.id.toLong())
        statement.bindText(2, entity.title)
        val _tmp: Int = if (entity.completed) 1 else 0
        statement.bindLong(3, _tmp.toLong())
        val _tmpPlannedDate: Date? = entity.plannedDate
        val _tmp_1: Long? = __dateConverter.dateToTimestamp(_tmpPlannedDate)
        if (_tmp_1 == null) {
          statement.bindNull(4)
        } else {
          statement.bindLong(4, _tmp_1)
        }
        val _tmpPlannedStartTime: Date? = entity.plannedStartTime
        val _tmp_2: Long? = __dateConverter.dateToTimestamp(_tmpPlannedStartTime)
        if (_tmp_2 == null) {
          statement.bindNull(5)
        } else {
          statement.bindLong(5, _tmp_2)
        }
        val _tmpPlannedEndTime: Date? = entity.plannedEndTime
        val _tmp_3: Long? = __dateConverter.dateToTimestamp(_tmpPlannedEndTime)
        if (_tmp_3 == null) {
          statement.bindNull(6)
        } else {
          statement.bindLong(6, _tmp_3)
        }
        statement.bindLong(7, entity.id.toLong())
      }
    }
  }

  public override suspend fun insert(todo: Todo): Unit = performSuspending(__db, false, true) { _connection ->
    __insertAdapterOfTodo.insert(_connection, todo)
  }

  public override suspend fun delete(todo: Todo): Unit = performSuspending(__db, false, true) { _connection ->
    __deleteAdapterOfTodo.handle(_connection, todo)
  }

  public override suspend fun update(todo: Todo): Unit = performSuspending(__db, false, true) { _connection ->
    __updateAdapterOfTodo.handle(_connection, todo)
  }

  public override fun getAllTodos(): Flow<List<Todo>> {
    val _sql: String = "SELECT * from todos ORDER BY completed ASC, id DESC"
    return createFlow(__db, false, arrayOf("todos")) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfCompleted: Int = getColumnIndexOrThrow(_stmt, "completed")
        val _columnIndexOfPlannedDate: Int = getColumnIndexOrThrow(_stmt, "plannedDate")
        val _columnIndexOfPlannedStartTime: Int = getColumnIndexOrThrow(_stmt, "plannedStartTime")
        val _columnIndexOfPlannedEndTime: Int = getColumnIndexOrThrow(_stmt, "plannedEndTime")
        val _result: MutableList<Todo> = mutableListOf()
        while (_stmt.step()) {
          val _item: Todo
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfCompleted).toInt()
          _tmpCompleted = _tmp != 0
          val _tmpPlannedDate: Date?
          val _tmp_1: Long?
          if (_stmt.isNull(_columnIndexOfPlannedDate)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_columnIndexOfPlannedDate)
          }
          _tmpPlannedDate = __dateConverter.fromTimestamp(_tmp_1)
          val _tmpPlannedStartTime: Date?
          val _tmp_2: Long?
          if (_stmt.isNull(_columnIndexOfPlannedStartTime)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getLong(_columnIndexOfPlannedStartTime)
          }
          _tmpPlannedStartTime = __dateConverter.fromTimestamp(_tmp_2)
          val _tmpPlannedEndTime: Date?
          val _tmp_3: Long?
          if (_stmt.isNull(_columnIndexOfPlannedEndTime)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getLong(_columnIndexOfPlannedEndTime)
          }
          _tmpPlannedEndTime = __dateConverter.fromTimestamp(_tmp_3)
          _item = Todo(_tmpId,_tmpTitle,_tmpCompleted,_tmpPlannedDate,_tmpPlannedStartTime,_tmpPlannedEndTime)
          _result.add(_item)
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public override suspend fun getTodo(id: Int): Todo? {
    val _sql: String = "SELECT * from todos WHERE id = ?"
    return performSuspending(__db, true, false) { _connection ->
      val _stmt: SQLiteStatement = _connection.prepare(_sql)
      try {
        var _argIndex: Int = 1
        _stmt.bindLong(_argIndex, id.toLong())
        val _columnIndexOfId: Int = getColumnIndexOrThrow(_stmt, "id")
        val _columnIndexOfTitle: Int = getColumnIndexOrThrow(_stmt, "title")
        val _columnIndexOfCompleted: Int = getColumnIndexOrThrow(_stmt, "completed")
        val _columnIndexOfPlannedDate: Int = getColumnIndexOrThrow(_stmt, "plannedDate")
        val _columnIndexOfPlannedStartTime: Int = getColumnIndexOrThrow(_stmt, "plannedStartTime")
        val _columnIndexOfPlannedEndTime: Int = getColumnIndexOrThrow(_stmt, "plannedEndTime")
        val _result: Todo?
        if (_stmt.step()) {
          val _tmpId: Int
          _tmpId = _stmt.getLong(_columnIndexOfId).toInt()
          val _tmpTitle: String
          _tmpTitle = _stmt.getText(_columnIndexOfTitle)
          val _tmpCompleted: Boolean
          val _tmp: Int
          _tmp = _stmt.getLong(_columnIndexOfCompleted).toInt()
          _tmpCompleted = _tmp != 0
          val _tmpPlannedDate: Date?
          val _tmp_1: Long?
          if (_stmt.isNull(_columnIndexOfPlannedDate)) {
            _tmp_1 = null
          } else {
            _tmp_1 = _stmt.getLong(_columnIndexOfPlannedDate)
          }
          _tmpPlannedDate = __dateConverter.fromTimestamp(_tmp_1)
          val _tmpPlannedStartTime: Date?
          val _tmp_2: Long?
          if (_stmt.isNull(_columnIndexOfPlannedStartTime)) {
            _tmp_2 = null
          } else {
            _tmp_2 = _stmt.getLong(_columnIndexOfPlannedStartTime)
          }
          _tmpPlannedStartTime = __dateConverter.fromTimestamp(_tmp_2)
          val _tmpPlannedEndTime: Date?
          val _tmp_3: Long?
          if (_stmt.isNull(_columnIndexOfPlannedEndTime)) {
            _tmp_3 = null
          } else {
            _tmp_3 = _stmt.getLong(_columnIndexOfPlannedEndTime)
          }
          _tmpPlannedEndTime = __dateConverter.fromTimestamp(_tmp_3)
          _result = Todo(_tmpId,_tmpTitle,_tmpCompleted,_tmpPlannedDate,_tmpPlannedStartTime,_tmpPlannedEndTime)
        } else {
          _result = null
        }
        _result
      } finally {
        _stmt.close()
      }
    }
  }

  public companion object {
    public fun getRequiredConverters(): List<KClass<*>> = emptyList()
  }
}
