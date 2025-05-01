package com.example.savora.transaction

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import java.util.Date

@Dao
interface TransactionDao {

    //Insert or update transaction
    @Upsert
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM transaction_table WHERE date BETWEEN :startDate AND :endDate " +
            "AND startTime >= :startTime AND endTime <= :endTime ORDER BY date DESC")
    suspend fun getExpensesInPeriod(startDate: Date, endDate: Date, startTime: Date, endTime: Date): List<Transaction>

    @Query("SELECT SUM(amount) FROM transaction_table")
    fun getTotalExpensesAmount(): Float

 @Query("SELECT * FROM transaction_table ORDER BY date ASC")
  suspend fun getAllTransactions(): List<Transaction>

    @Delete
    suspend fun delete(transaction: Transaction)
}