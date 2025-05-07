package com.github.sendiko.penghitungsembako.sembako.core.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface SembakoDao {

    @Query("SELECT * FROM sembako")
    fun getAll(): Flow<List<Sembako>>

    @Query("SELECT * FROM sembako WHERE id = :id")
    suspend fun getById(id: Int): Sembako

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sembako: Sembako)

    @Query("DELETE FROM sembako WHERE id = :id")
    suspend fun delete(id: Int)

    @Update
    suspend fun update(sembako: Sembako)

}