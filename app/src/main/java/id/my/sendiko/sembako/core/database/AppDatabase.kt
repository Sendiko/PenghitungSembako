package id.my.sendiko.sembako.core.database

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import id.my.sendiko.sembako.grocery.core.data.GroceryEntity
import id.my.sendiko.sembako.grocery.core.data.GroceryDao
import id.my.sendiko.sembako.history.data.HistoryDao
import id.my.sendiko.sembako.history.data.HistoryEntity

@Database(
    entities = [GroceryEntity::class, HistoryEntity::class],
    version = 5,
    autoMigrations = [AutoMigration(from = 4, to = 5)],
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract val sembakoDao: GroceryDao
    abstract val historyDao: HistoryDao

}