package com.example.savora.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class TransactionViewModel(private val dao: TransactionDao) : ViewModel() {

    private val _state = MutableStateFlow(TransactionState())
    val state: StateFlow<TransactionState> = _state


    fun onEvent(event: TransactionEvent) {
        when (event) {

            is TransactionEvent.GetAllTransactions -> {
                viewModelScope.launch {
                    val userTransactions = dao.getAllTransactions(event.userId)
                    _state.value = _state.value.copy(
                        transactions = userTransactions,
                        userId = event.userId
                    )
                }
            }

            is TransactionEvent.DeleteTransaction -> {
                viewModelScope.launch {
                    dao.delete(event.transaction)
                }
            }

            is TransactionEvent.SetUserId -> {
                _state.update { it.copy(userId = event.userId) }
            }

            is TransactionEvent.SetAmount -> {
                _state.update { it.copy(amount = event.amount) }
            }

            is TransactionEvent.SetDate -> {
                _state.update { it.copy(date = event.date) }
            }

            is TransactionEvent.SetDescription -> {
                _state.update { it.copy(description = event.description) }
            }

            is TransactionEvent.SetStartTime -> {
                _state.update { it.copy(startTime = event.startTime) }
            }

            is TransactionEvent.SetEndTime -> {
                _state.update { it.copy(endTime = event.endTime) }
            }

            is TransactionEvent.SetReceiptPhoto -> {
                _state.update { it.copy(receiptPhoto = event.receiptPhoto) }
            }

            TransactionEvent.ShowDialog -> {
                _state.update { it.copy(isAddingTransaction = true) }
            }

            TransactionEvent.HideDialog -> {
                _state.update { it.copy(isAddingTransaction = false) }
            }

            TransactionEvent.SaveTransaction -> {
                val currentState = _state.value
                if (currentState.amount <= 0 || currentState.categoryId == 0) return

                val transaction = Transaction(
                    userId = currentState.userId,
                    amount = currentState.amount,
                    date = currentState.date,
                    description = currentState.description,
                    categoryId = currentState.categoryId,
                    startTime = currentState.startTime,
                    endTime = currentState.endTime,
                    receiptPhoto = currentState.receiptPhoto,
                )

                viewModelScope.launch {
                    dao.insert(transaction)
                }

                _state.update {
                    it.copy(userId = 0, amount = 0.0, date = Date(), description = "",
                        categoryId = 0, startTime = Date(), endTime = Date(), receiptPhoto = null,
                        isAddingTransaction = false)
                }
            }
        }
    }
}
