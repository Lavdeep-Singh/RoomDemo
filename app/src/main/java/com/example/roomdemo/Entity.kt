package com.example.roomdemo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emp")
data class Entity (
    @PrimaryKey(autoGenerate = true) val id:Int,
    val name:String,
    val email:String
        )