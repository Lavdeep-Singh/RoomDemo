package com.example.roomdemo

import androidx.room.*
import androidx.room.Dao

@Dao
interface Dao {
    @Insert
    fun insertData(entity: Entity)

    @Delete
    fun deleteData(entity: Entity)

    @Update
    fun updateData(entity: Entity)

    @Query("select * from emp")
    fun getAllData():List<Entity>
}