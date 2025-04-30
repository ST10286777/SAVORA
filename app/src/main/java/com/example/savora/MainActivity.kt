package com.example.savora

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.content.Intent
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

            val btnGoToLogin = findViewById<Button>(R.id.btnGoToLogin)

            btnGoToLogin.setOnClickListener {
                // Create an Intent to navigate to LoginRegisterActivity
                val intent = Intent(this, LoginRegisterActivity::class.java)
                startActivity(intent)}
        }
    }
