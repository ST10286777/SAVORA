package com.example.savora.transaction

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.savora.User.User
import com.example.savora.category.Category
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.Date

@Entity(
    tableName = "Transaction_table",
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val userId: Int,
    val amount: Double,
    val date: Date,
    val description: String,
    val categoryId: Int,
    val startTime: Date,
    val endTime: Date,
    val receiptPhoto: String? = null,
    val category: String
)

