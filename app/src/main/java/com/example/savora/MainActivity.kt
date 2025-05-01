package com.example.savora

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.Button
import com.example.savora.User.LoginActivity
import com.example.savora.category.CategoryActivity
import com.example.savora.transaction.AddTransactionActivity
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val btnGoToLogin = findViewById<Button>(R.id.btnAddCategory)

        btnGoToLogin.setOnClickListener {
            // Create an Intent to navigate to CategoryActivity
            val intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        true
                }

                R.id.nav_expenses -> {
                    // Load Dashboard
                    true
                }

                R.id.nav_budget -> {
                    // Load Dashboard
                    true
                }

                R.id.nav_new_trans -> {
                    // Loads AddTransactionActivity
                    val intent = Intent(this, AddTransactionActivity::class.java)
                    startActivity(intent)
                    true
                    true
                }

                R.id.nav_profile -> {
                    // Load Profile
                    true
                }

                else -> false
            }
        }

    }
}
