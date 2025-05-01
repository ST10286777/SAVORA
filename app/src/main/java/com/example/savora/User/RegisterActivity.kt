package com.example.savora.User

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

class RegisterActivity : AppCompatActivity()
{
    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var backButton: Button
    private lateinit var registerButton: Button



    // Get ViewModel using a factory
    private val viewModel: UserViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val db = AppDatabase.getDatabase(applicationContext)
                val dao = db.UserDao()
                return UserViewModel(dao) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        usernameInput = findViewById(R.id.etUsername)
        passwordInput = findViewById(R.id.etPassword)
        backButton = findViewById(R.id.btnBack)
        registerButton = findViewById(R.id.btnRegister)

        // Handling login
        backButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Handling register
        registerButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                viewModel.onEvent(UserEvent.SetUserName(username))
                viewModel.onEvent(UserEvent.SetPassword(password))
                viewModel.onEvent(UserEvent.SaveUser)
                Toast.makeText(this, "User Registered!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@RegisterActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show()
            }
        }

   }

}