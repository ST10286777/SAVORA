package com.example.savora

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.ViewModelProvider
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginRegisterActivity : AppCompatActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button
    private lateinit var resultText: TextView

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
        setContentView(R.layout.activity_login_register)

        usernameInput = findViewById(R.id.etUsername)
        passwordInput = findViewById(R.id.etPassword)
        loginButton = findViewById(R.id.btnLogin)
        registerButton = findViewById(R.id.btnRegister)
        resultText = findViewById(R.id.tvResult)

        // Observing login result
        lifecycleScope.launch {
            viewModel.loginResult.collectLatest { success ->
                if (success == true) {
                    resultText.text = "Login Successful!"
                } else if (success == false) {
                    resultText.text = "Login Failed. Check credentials."
                }
            }
        }

        // Handling login
        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            if (username.isNotBlank() && password.isNotBlank()) {
                viewModel.onEvent(UserEvent.login(username, password))
            } else {
                Toast.makeText(this, "Please enter both fields", Toast.LENGTH_SHORT).show()
            }
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
            } else {
                Toast.makeText(this, "Please fill both fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
