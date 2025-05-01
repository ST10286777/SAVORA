package com.example.savora

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.savora.User.User
import com.example.savora.User.UserDao
import com.example.savora.category.Category
import com.example.savora.category.CategoryDao
import com.example.savora.transaction.Transaction
import com.example.savora.transaction.TransactionDao

@Database(entities = [User::class, Category::class, Transaction::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun UserDao(): UserDao
    abstract fun CategoryDao(): CategoryDao
    abstract fun TransactionDao(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "savora_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
