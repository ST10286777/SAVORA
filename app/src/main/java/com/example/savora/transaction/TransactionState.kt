package com.example.savora.transaction

import java.util.Date


data class TransactionState(
    val transactions: List<Transaction> = emptyList(),
    val userId: Int = 0,
    val amount: Double = 0.0,
    val date: Date = Date(), // Default to current Date
    val description: String = "",
    val category: String = "",
    val categoryId: Int = 0,
    val startTime: Date = Date(),  // Default to current time
    val endTime: Date = Date(System.currentTimeMillis() + 3600000), // Default +1 hour
    val receiptPhoto: String? = null,
    val isAddingTransaction: Boolean = false
)