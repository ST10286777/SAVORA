package com.example.savora.transaction

import java.util.Date

interface TransactionEvent
{
    object SaveTransaction: TransactionEvent
    data class SetUserId (val userId : Int) : TransactionEvent
    data class SetCategoryId (val userId : Int) : TransactionEvent
    data class SetAmount (val amount : Double) : TransactionEvent
    data class SetDate (val date : Date) : TransactionEvent
    data class SetDescription (val description: String) : TransactionEvent
    data class SetStartTime(val startTime: Date) : TransactionEvent
    data class SetEndTime(val endTime: Date) : TransactionEvent
    data class SetReceiptPhoto (val receiptPhoto: String?) : TransactionEvent
    object ShowDialog: TransactionEvent
    object HideDialog: TransactionEvent
    data class  DeleteTransaction(val transaction: Transaction): TransactionEvent

}