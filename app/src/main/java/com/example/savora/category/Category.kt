package com.example.savora.category

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Category_table")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String
)
