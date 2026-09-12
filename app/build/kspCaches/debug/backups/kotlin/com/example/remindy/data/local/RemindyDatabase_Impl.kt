package com.example.remindy.`data`.local

import androidx.room.InvalidationTracker
import androidx.room.RoomOpenDelegate
import androidx.room.migration.AutoMigrationSpec
import androidx.room.migration.Migration
import androidx.room.util.TableInfo
import androidx.room.util.TableInfo.Companion.read
import androidx.room.util.dropFtsSyncTriggers
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL
import com.example.remindy.`data`.local.dao.ReminderDao
import com.example.remindy.`data`.local.dao.ReminderDao_Impl
import com.example.remindy.`data`.local.dao.SettingDao
import com.example.remindy.`data`.local.dao.SettingDao_Impl
import com.example.remindy.`data`.local.dao.StudyItemDao
import com.example.remindy.`data`.local.dao.StudyItemDao_Impl
import com.example.remindy.`data`.local.dao.SyncMetadataDao
import com.example.remindy.`data`.local.dao.SyncMetadataDao_Impl
import com.example.remindy.`data`.local.dao.TodoDao
import com.example.remindy.`data`.local.dao.TodoDao_Impl
import javax.`annotation`.processing.Generated
import kotlin.Lazy
import kotlin.String
import kotlin.Suppress
import kotlin.collections.List
import kotlin.collections.Map
import kotlin.collections.MutableList
import kotlin.collections.MutableMap
import kotlin.collections.MutableSet
import kotlin.collections.Set
import kotlin.collections.mutableListOf
import kotlin.collections.mutableMapOf
import kotlin.collections.mutableSetOf
import kotlin.reflect.KClass

@Generated(value = ["androidx.room.RoomProcessor"])
@Suppress(names = ["UNCHECKED_CAST", "DEPRECATION", "REDUNDANT_PROJECTION", "REMOVAL"])
public class RemindyDatabase_Impl : RemindyDatabase() {
  private val _reminderDao: Lazy<ReminderDao> = lazy {
    ReminderDao_Impl(this)
  }

  private val _studyItemDao: Lazy<StudyItemDao> = lazy {
    StudyItemDao_Impl(this)
  }

  private val _settingDao: Lazy<SettingDao> = lazy {
    SettingDao_Impl(this)
  }

  private val _syncMetadataDao: Lazy<SyncMetadataDao> = lazy {
    SyncMetadataDao_Impl(this)
  }

  private val _todoDao: Lazy<TodoDao> = lazy {
    TodoDao_Impl(this)
  }

  protected override fun createOpenDelegate(): RoomOpenDelegate {
    val _openDelegate: RoomOpenDelegate = object : RoomOpenDelegate(3,
        "081b680adf987197748dc0ba929c049a", "02831312e2a6e53d1d42ec085e8071ba") {
      public override fun createAllTables(connection: SQLiteConnection) {
        connection.execSQL("CREATE TABLE IF NOT EXISTS `reminders` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `scheduleType` TEXT NOT NULL, `scheduleTime` TEXT NOT NULL, `scheduleDate` TEXT, `scheduleDayOfWeek` TEXT, `scheduleDayOfMonth` INTEGER, `enabled` INTEGER NOT NULL, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, `deletedAt` TEXT, `synced` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `study_items` (`id` TEXT NOT NULL, `kind` TEXT NOT NULL, `prompt` TEXT NOT NULL, `answer` TEXT NOT NULL, `enabled` INTEGER NOT NULL, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, `deletedAt` TEXT, `synced` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `notification_setting` (`id` INTEGER NOT NULL, `frequency` TEXT NOT NULL, `enabled` INTEGER NOT NULL, `updatedAt` TEXT NOT NULL, `synced` INTEGER NOT NULL, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `sync_metadata` (`id` INTEGER NOT NULL, `lastSyncedAt` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS `todos` (`id` TEXT NOT NULL, `title` TEXT NOT NULL, `description` TEXT NOT NULL, `createdAt` TEXT NOT NULL, `updatedAt` TEXT NOT NULL, `deletedAt` TEXT, PRIMARY KEY(`id`))")
        connection.execSQL("CREATE TABLE IF NOT EXISTS room_master_table (id INTEGER PRIMARY KEY,identity_hash TEXT)")
        connection.execSQL("INSERT OR REPLACE INTO room_master_table (id,identity_hash) VALUES(42, '081b680adf987197748dc0ba929c049a')")
      }

      public override fun dropAllTables(connection: SQLiteConnection) {
        connection.execSQL("DROP TABLE IF EXISTS `reminders`")
        connection.execSQL("DROP TABLE IF EXISTS `study_items`")
        connection.execSQL("DROP TABLE IF EXISTS `notification_setting`")
        connection.execSQL("DROP TABLE IF EXISTS `sync_metadata`")
        connection.execSQL("DROP TABLE IF EXISTS `todos`")
      }

      public override fun onCreate(connection: SQLiteConnection) {
      }

      public override fun onOpen(connection: SQLiteConnection) {
        internalInitInvalidationTracker(connection)
      }

      public override fun onPreMigrate(connection: SQLiteConnection) {
        dropFtsSyncTriggers(connection)
      }

      public override fun onPostMigrate(connection: SQLiteConnection) {
      }

      public override fun onValidateSchema(connection: SQLiteConnection):
          RoomOpenDelegate.ValidationResult {
        val _columnsReminders: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsReminders.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReminders.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReminders.put("scheduleType", TableInfo.Column("scheduleType", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReminders.put("scheduleTime", TableInfo.Column("scheduleTime", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReminders.put("scheduleDate", TableInfo.Column("scheduleDate", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReminders.put("scheduleDayOfWeek", TableInfo.Column("scheduleDayOfWeek", "TEXT",
            false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReminders.put("scheduleDayOfMonth", TableInfo.Column("scheduleDayOfMonth",
            "INTEGER", false, 0, null, TableInfo.CREATED_FROM_ENTITY))
        _columnsReminders.put("enabled", TableInfo.Column("enabled", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReminders.put("createdAt", TableInfo.Column("createdAt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReminders.put("updatedAt", TableInfo.Column("updatedAt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReminders.put("deletedAt", TableInfo.Column("deletedAt", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsReminders.put("synced", TableInfo.Column("synced", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysReminders: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesReminders: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoReminders: TableInfo = TableInfo("reminders", _columnsReminders,
            _foreignKeysReminders, _indicesReminders)
        val _existingReminders: TableInfo = read(connection, "reminders")
        if (!_infoReminders.equals(_existingReminders)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |reminders(com.example.remindy.data.local.entity.ReminderEntity).
              | Expected:
              |""".trimMargin() + _infoReminders + """
              |
              | Found:
              |""".trimMargin() + _existingReminders)
        }
        val _columnsStudyItems: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsStudyItems.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStudyItems.put("kind", TableInfo.Column("kind", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStudyItems.put("prompt", TableInfo.Column("prompt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStudyItems.put("answer", TableInfo.Column("answer", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStudyItems.put("enabled", TableInfo.Column("enabled", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStudyItems.put("createdAt", TableInfo.Column("createdAt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStudyItems.put("updatedAt", TableInfo.Column("updatedAt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStudyItems.put("deletedAt", TableInfo.Column("deletedAt", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsStudyItems.put("synced", TableInfo.Column("synced", "INTEGER", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysStudyItems: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesStudyItems: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoStudyItems: TableInfo = TableInfo("study_items", _columnsStudyItems,
            _foreignKeysStudyItems, _indicesStudyItems)
        val _existingStudyItems: TableInfo = read(connection, "study_items")
        if (!_infoStudyItems.equals(_existingStudyItems)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |study_items(com.example.remindy.data.local.entity.StudyItemEntity).
              | Expected:
              |""".trimMargin() + _infoStudyItems + """
              |
              | Found:
              |""".trimMargin() + _existingStudyItems)
        }
        val _columnsNotificationSetting: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsNotificationSetting.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsNotificationSetting.put("frequency", TableInfo.Column("frequency", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotificationSetting.put("enabled", TableInfo.Column("enabled", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotificationSetting.put("updatedAt", TableInfo.Column("updatedAt", "TEXT", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        _columnsNotificationSetting.put("synced", TableInfo.Column("synced", "INTEGER", true, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysNotificationSetting: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesNotificationSetting: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoNotificationSetting: TableInfo = TableInfo("notification_setting",
            _columnsNotificationSetting, _foreignKeysNotificationSetting,
            _indicesNotificationSetting)
        val _existingNotificationSetting: TableInfo = read(connection, "notification_setting")
        if (!_infoNotificationSetting.equals(_existingNotificationSetting)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |notification_setting(com.example.remindy.data.local.entity.SettingEntity).
              | Expected:
              |""".trimMargin() + _infoNotificationSetting + """
              |
              | Found:
              |""".trimMargin() + _existingNotificationSetting)
        }
        val _columnsSyncMetadata: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsSyncMetadata.put("id", TableInfo.Column("id", "INTEGER", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsSyncMetadata.put("lastSyncedAt", TableInfo.Column("lastSyncedAt", "TEXT", false, 0,
            null, TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysSyncMetadata: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesSyncMetadata: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoSyncMetadata: TableInfo = TableInfo("sync_metadata", _columnsSyncMetadata,
            _foreignKeysSyncMetadata, _indicesSyncMetadata)
        val _existingSyncMetadata: TableInfo = read(connection, "sync_metadata")
        if (!_infoSyncMetadata.equals(_existingSyncMetadata)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |sync_metadata(com.example.remindy.data.local.entity.SyncMetadataEntity).
              | Expected:
              |""".trimMargin() + _infoSyncMetadata + """
              |
              | Found:
              |""".trimMargin() + _existingSyncMetadata)
        }
        val _columnsTodos: MutableMap<String, TableInfo.Column> = mutableMapOf()
        _columnsTodos.put("id", TableInfo.Column("id", "TEXT", true, 1, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTodos.put("title", TableInfo.Column("title", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTodos.put("description", TableInfo.Column("description", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTodos.put("createdAt", TableInfo.Column("createdAt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTodos.put("updatedAt", TableInfo.Column("updatedAt", "TEXT", true, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        _columnsTodos.put("deletedAt", TableInfo.Column("deletedAt", "TEXT", false, 0, null,
            TableInfo.CREATED_FROM_ENTITY))
        val _foreignKeysTodos: MutableSet<TableInfo.ForeignKey> = mutableSetOf()
        val _indicesTodos: MutableSet<TableInfo.Index> = mutableSetOf()
        val _infoTodos: TableInfo = TableInfo("todos", _columnsTodos, _foreignKeysTodos,
            _indicesTodos)
        val _existingTodos: TableInfo = read(connection, "todos")
        if (!_infoTodos.equals(_existingTodos)) {
          return RoomOpenDelegate.ValidationResult(false, """
              |todos(com.example.remindy.data.local.entity.TodoEntity).
              | Expected:
              |""".trimMargin() + _infoTodos + """
              |
              | Found:
              |""".trimMargin() + _existingTodos)
        }
        return RoomOpenDelegate.ValidationResult(true, null)
      }
    }
    return _openDelegate
  }

  protected override fun createInvalidationTracker(): InvalidationTracker {
    val _shadowTablesMap: MutableMap<String, String> = mutableMapOf()
    val _viewTables: MutableMap<String, Set<String>> = mutableMapOf()
    return InvalidationTracker(this, _shadowTablesMap, _viewTables, "reminders", "study_items",
        "notification_setting", "sync_metadata", "todos")
  }

  public override fun clearAllTables() {
    super.performClear(false, "reminders", "study_items", "notification_setting", "sync_metadata",
        "todos")
  }

  protected override fun getRequiredTypeConverterClasses(): Map<KClass<*>, List<KClass<*>>> {
    val _typeConvertersMap: MutableMap<KClass<*>, List<KClass<*>>> = mutableMapOf()
    _typeConvertersMap.put(ReminderDao::class, ReminderDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(StudyItemDao::class, StudyItemDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SettingDao::class, SettingDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(SyncMetadataDao::class, SyncMetadataDao_Impl.getRequiredConverters())
    _typeConvertersMap.put(TodoDao::class, TodoDao_Impl.getRequiredConverters())
    return _typeConvertersMap
  }

  public override fun getRequiredAutoMigrationSpecClasses(): Set<KClass<out AutoMigrationSpec>> {
    val _autoMigrationSpecsSet: MutableSet<KClass<out AutoMigrationSpec>> = mutableSetOf()
    return _autoMigrationSpecsSet
  }

  public override
      fun createAutoMigrations(autoMigrationSpecs: Map<KClass<out AutoMigrationSpec>, AutoMigrationSpec>):
      List<Migration> {
    val _autoMigrations: MutableList<Migration> = mutableListOf()
    return _autoMigrations
  }

  public override fun reminderDao(): ReminderDao = _reminderDao.value

  public override fun studyItemDao(): StudyItemDao = _studyItemDao.value

  public override fun settingDao(): SettingDao = _settingDao.value

  public override fun syncMetadataDao(): SyncMetadataDao = _syncMetadataDao.value

  public override fun todoDao(): TodoDao = _todoDao.value
}
