package com.github.sendiko.penghitungsembako.grocery.core.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface GroceryDao {

    @Query("SELECT * FROM grocery")
    fun getAll(): Flow<List<GroceryEntity>>

    @Query("SELECT * FROM grocery WHERE id = :id")
    suspend fun getById(id: Int): GroceryEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sembako: GroceryEntity)

    @Query("DELETE FROM grocery WHERE id = :id")
    suspend fun delete(id: Int)

    @Query("DELETE FROM grocery")
    suspend fun deleteAll()

    @Update
    suspend fun update(sembako: GroceryEntity)

}