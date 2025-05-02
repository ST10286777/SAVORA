package com.example.savora.transaction

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.savora.AppDatabase
import com.example.savora.R
import kotlinx.coroutines.launch

class DisplayTransactionsActivity : AppCompatActivity() {

    private val viewModel: TransactionViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val db = AppDatabase.getDatabase(applicationContext)
                val dao = db.TransactionDao()
                return TransactionViewModel(dao) as T
            }
        }
    }

    private lateinit var adapter: TransactionAdapter
    private lateinit var btnAddTransaction: Button

    private val userId: Int by lazy {
        intent.getIntExtra("userId", getCurrentUserId())
    }

    private fun getCurrentUserId(): Int {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPreferences.getInt("user_id", 0)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_transactions)

        btnAddTransaction = findViewById(R.id.btnAddTransaction)
        val recyclerView = findViewById<RecyclerView>(R.id.rvTransactions)

        adapter = TransactionAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Navigate to AddTransactionActivity when the button is clicked
        btnAddTransaction.setOnClickListener {
            val intent = Intent(this, AddTransactionActivity::class.java)
            intent.putExtra("userId", userId)
            startActivity(intent)
        }

        // Load transactions for the user
        viewModel.onEvent(TransactionEvent.GetAllTransactions(userId))

        // Observe transaction state and update UI
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                adapter.submitList(state.transactions)
            }
        }
    }
}
