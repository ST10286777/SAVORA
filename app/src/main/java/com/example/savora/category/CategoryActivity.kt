package com.example.savora.category

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.savora.AppDatabase
import com.example.savora.MainActivity
import com.example.savora.R

class CategoryActivity : AppCompatActivity()
{
    private lateinit var categoryInput: EditText
    private lateinit var saveButton: Button
    private lateinit var backButton: Button

    // Get ViewModel using a factory
    private val viewModel: CategoryViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val db = AppDatabase.getDatabase(applicationContext)
                val dao = db.CategoryDao()
                return CategoryViewModel(dao) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        categoryInput = findViewById(R.id.etCategory)
        backButton = findViewById(R.id.btnBack)
        saveButton = findViewById(R.id.btnSave)

        // Navigates back to Home
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Saves Category
        saveButton.setOnClickListener {
            val category = categoryInput.text.toString()

            if (category.isNotBlank()) {
                viewModel.onEvent(CategoryEvent.SetCategory(category))
                viewModel.onEvent(CategoryEvent.SaveCategory)
                Toast.makeText(this, "New Category Added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please Enter a Category", Toast.LENGTH_SHORT).show()
            }
        }
    }
}