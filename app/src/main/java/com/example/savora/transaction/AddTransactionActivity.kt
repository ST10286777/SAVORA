package com.example.savora.transaction

import android.Manifest
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.savora.AppDatabase
import com.example.savora.MainActivity
import com.example.savora.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddTransactionActivity : AppCompatActivity()
{
    private lateinit var etAmount: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var etDescription: EditText
    private lateinit var btnSelectDate: Button
    private lateinit var tvDate: TextView
    private lateinit var btnSaveTransaction: Button
    private lateinit var btnAttachReceipt: Button
    private lateinit var btnCapturePhoto: Button
    private lateinit var btnSelectStartTime: Button
    private lateinit var btnSelectEndTime: Button
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView
    private val categoryMap = mutableMapOf<String, Int>()
    private var selectedDateMillis: Long = System.currentTimeMillis()
    private var currentPhotoPath: String? = null
    private var receiptUri: String? = null

    // Get ViewModel using a factory
    private val viewModel: TransactionViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : androidx.lifecycle.ViewModel> create(modelClass: Class<T>): T {
                val db = AppDatabase.getDatabase(applicationContext)
                val dao = db.TransactionDao()
                return TransactionViewModel(dao) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_transaction)

        // Permissions
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            ),
            1001
        )
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
                }

                R.id.nav_profile -> {
                    // Load Profile
                    true
                }

                else -> false
            }
        }

        // Bind views
        etAmount = findViewById(R.id.etAmount)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        etDescription = findViewById(R.id.etDescription)
        btnSelectDate = findViewById(R.id.btnSelectDate)
        tvDate = findViewById(R.id.tvDate)
        btnSaveTransaction = findViewById(R.id.btnSaveExpense)
        btnSelectStartTime = findViewById(R.id.btnSelectStartTime)
        btnSelectEndTime = findViewById(R.id.btnSelectEndTime)
        tvStartTime = findViewById(R.id.tvStartTime)
        tvEndTime = findViewById(R.id.tvEndTime)
        btnAttachReceipt = findViewById(R.id.btnAttachReceipt)
        btnCapturePhoto = findViewById(R.id.btnCapturePhoto)


        btnAttachReceipt.setOnClickListener { pickReceiptFromGallery() }
        btnCapturePhoto.setOnClickListener { dispatchTakePictureIntent() }


        // Existing flows
        loadCategories()
        btnSelectStartTime.setOnClickListener { showTimePicker(true) }
        btnSelectEndTime.setOnClickListener { showTimePicker(false) }
        btnSelectDate.setOnClickListener { showDatePicker() }
        btnSaveTransaction.setOnClickListener { saveTransaction() }
    }

    private fun showTimePicker(isStartTime: Boolean) {
        val calendar = Calendar.getInstance()

        // Create the time picker dialog with proper theme
        TimePickerDialog(
            this@AddTransactionActivity,
            { _, hourOfDay, minute ->
                // Create new Calendar instance with selected time
                val selectedTime = Calendar.getInstance().apply {
                    timeInMillis = selectedDateMillis // Maintain the selected date
                    set(Calendar.HOUR_OF_DAY, hourOfDay)
                    set(Calendar.MINUTE, minute)
                    set(Calendar.SECOND, 0)
                }
                if (isStartTime) {
                    tvStartTime.text = formatTime(hourOfDay, minute)
                    viewModel.onEvent(TransactionEvent.SetStartTime(selectedTime.time))
                } else {
                    tvEndTime.text = formatTime(hourOfDay, minute)
                    viewModel.onEvent(TransactionEvent.SetEndTime(selectedTime.time))
                }
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            false // 24-hour format
        ).apply {}.show()
    }

    private fun formatTime(hour: Int, minute: Int): String {
        return String.format("%02d:%02d", hour, minute)
    }

    private fun pickReceiptFromGallery() {
        startActivityForResult(
            Intent(Intent.ACTION_PICK).apply { type = "image/*" },
            2001
        )
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takeIntent ->
            takeIntent.resolveActivity(packageManager)?.also {
                createImageFile()?.also { file ->
                    val uri = FileProvider.getUriForFile(
                        this,
                        "${applicationContext.packageName}.fileprovider",
                        file
                    )
                    takeIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
                    startActivityForResult(takeIntent, 2002)
                }
            }
        }
    }

    private fun createImageFile(): File? {
        val southAfricaLocale = Locale("en", "ZA")
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", southAfricaLocale).format(Date())
        val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != RESULT_OK) return
        when (requestCode) {
            2001 -> data?.data?.let { uri ->
                receiptUri = uri.toString()
                Toast.makeText(this, "Receipt attached", Toast.LENGTH_SHORT).show()
            }
            2002 -> currentPhotoPath?.let { path ->
                receiptUri = path
                Toast.makeText(this, "Photo captured", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker() {
        val cal = Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                Calendar.getInstance().apply {
                    set(year, month, dayOfMonth)
                    selectedDateMillis = timeInMillis
                }
                // Format using SimpleDateFormat
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                tvDate.text = dateFormat.format(Date(selectedDateMillis))
            },
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun loadCategories() {
        lifecycleScope.launch(Dispatchers.IO) {
            val db = AppDatabase.getDatabase(applicationContext)
            val categories = db.CategoryDao().getAllCategories()
            val names = categories.map {
                categoryMap[it.category] = it.id
                it.category
            }
            withContext(Dispatchers.Main) {
                spinnerCategory.adapter = ArrayAdapter(
                    this@AddTransactionActivity,
                    R.layout.spinner_item,
                    names
                ).also { adapter ->
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
            }
        }
    }

    private fun getCurrentUserId(): Int {
        val sharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return sharedPreferences.getInt("user_id", 0)
    }

    private fun saveTransaction() {
        try {
            // Retrieve and validate input fields
            val amountText = etAmount.text.toString()
            val description = etDescription.text.toString()
            val categoryName = spinnerCategory.selectedItem?.toString()

            // Validate input fields
            if (amountText.isBlank() || categoryName.isNullOrBlank()) {
                Toast.makeText(this, "Please enter an amount and select a category.", Toast.LENGTH_SHORT).show()
                return
            }

            // Parse amount
            val amount = amountText.toDoubleOrNull()
            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Please enter a valid amount.", Toast.LENGTH_SHORT).show()
                return
            }

            // Get categoryId from categoryMap
            val categoryId = categoryMap[categoryName]
            if (categoryId == null) {
                Toast.makeText(this, "Selected category not found.", Toast.LENGTH_SHORT).show()
                return
            }

            // Get userId
            val userId = getCurrentUserId()

            // Retrieve start and end times from ViewModel state
            val startTime = viewModel.state.value.startTime
            val endTime = viewModel.state.value.endTime

            // Validate that endTime is after startTime
            if (startTime.after(endTime)) {
                Toast.makeText(this, "End time must be after start time", Toast.LENGTH_SHORT).show()
                return
            }

            // Dispatch events to update ViewModel state
            viewModel.onEvent(TransactionEvent.SetUserId(userId))
            viewModel.onEvent(TransactionEvent.SetCategoryId(categoryId))
            viewModel.onEvent(TransactionEvent.SetAmount(amount))
            viewModel.onEvent(TransactionEvent.SetDescription(description))
            viewModel.onEvent(TransactionEvent.SetDate(Date(selectedDateMillis)))
            viewModel.onEvent(TransactionEvent.SetStartTime(startTime))
            viewModel.onEvent(TransactionEvent.SetEndTime(endTime))
            viewModel.onEvent(TransactionEvent.SetReceiptPhoto(receiptUri))

            // Trigger the save action
            viewModel.onEvent(TransactionEvent.SaveTransaction)

            // Inform the user that the transaction has been saved
            Toast.makeText(this, "Transaction saved successfully.", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Log.e("saveTransaction", "Failed to save transaction: ${e.message}", e)
            Toast.makeText(this, "An error occurred while saving the transaction.", Toast.LENGTH_LONG).show()
        }
    }




}