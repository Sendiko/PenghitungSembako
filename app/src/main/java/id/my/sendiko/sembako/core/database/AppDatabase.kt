package id.my.sendiko.sembako.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import id.my.sendiko.sembako.grocery.core.data.GroceryEntity
import id.my.sendiko.sembako.grocery.core.data.GroceryDao

@Database(
    entities = [GroceryEntity::class],
    version = 4
)
abstract class AppDatabase : RoomDatabase() {

    abstract val sembakoDao: GroceryDao

}