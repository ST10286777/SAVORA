package com.example.savora

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "User_table")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val username: String,
    val password: String
)
