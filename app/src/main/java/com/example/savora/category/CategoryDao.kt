package com.example.savora.category

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface CategoryDao {
    @Upsert
    suspend fun insert(category: Category)

    @Query("SELECT * FROM Category_table")
    suspend fun getAllCategories(): List<Category>

    @Delete
    suspend fun delete(category: Category)

}