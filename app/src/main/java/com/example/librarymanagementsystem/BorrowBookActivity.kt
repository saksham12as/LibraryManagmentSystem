package com.example.librarymanagementsystem

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class BorrowBookActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var bookDropdown: Spinner
    private lateinit var borrowerNameInput: EditText
    private lateinit var fromDateInput: EditText
    private lateinit var toDateInput: EditText
    private lateinit var borrowBookButton: Button

    private var isEditMode = false
    private var borrowerIdToEdit: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_borrow_book)

        // Initialize Database Helper
        databaseHelper = DatabaseHelper(this)

        // Initialize Views
        bookDropdown = findViewById(R.id.bookDropdown)
        borrowerNameInput = findViewById(R.id.borrowerNameInput)
        fromDateInput = findViewById(R.id.fromDateInput)
        toDateInput = findViewById(R.id.toDateInput)
        borrowBookButton = findViewById(R.id.borrowBookButton)

        // Populate Dropdown with Book Titles
        val books = databaseHelper.getAllBooks()
        val bookTitles = books.map { it.bookName }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, bookTitles)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        bookDropdown.adapter = adapter

        // Check if in edit mode
        intent?.let {
            borrowerIdToEdit = it.getIntExtra("BORROWER_ID", -1)
            val borrowerName = it.getStringExtra("BORROWER_NAME")
            val borrowedBook = it.getStringExtra("BORROWED_BOOK")
            val borrowDate = it.getStringExtra("BORROW_DATE")
            val returnDate = it.getStringExtra("RETURN_DATE")

            if (borrowerIdToEdit != -1 && borrowerName != null && borrowedBook != null && borrowDate != null && returnDate != null) {
                isEditMode = true
                borrowerNameInput.setText(borrowerName)
                fromDateInput.setText(borrowDate)
                toDateInput.setText(returnDate)
                val bookPosition = bookTitles.indexOf(borrowedBook)
                if (bookPosition >= 0) {
                    bookDropdown.setSelection(bookPosition)
                }
                borrowBookButton.text = "Update Borrower"
            }
        }

        // DateTime Picker for fromDate and toDate
        fromDateInput.setOnClickListener { showDateTimePicker(fromDateInput) }
        toDateInput.setOnClickListener { showDateTimePicker(toDateInput) }

        // Handle Borrow or Update Button Click
        borrowBookButton.setOnClickListener {
            val selectedBook = bookDropdown.selectedItem.toString()
            val borrowerName = borrowerNameInput.text.toString()
            val fromDate = fromDateInput.text.toString()
            val toDate = toDateInput.text.toString()

            if (borrowerName.isEmpty() || fromDate.isEmpty() || toDate.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isEditMode) {
                // Update Borrower
                borrowerIdToEdit?.let { id ->
                    val success = databaseHelper.updateBorrower(id, borrowerName, selectedBook, fromDate, toDate)
                    if (success) {
                        Toast.makeText(this, "Borrower updated successfully", Toast.LENGTH_SHORT).show()
                        setResult(Activity.RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this, "Failed to update borrower", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                // Add New Borrower
                val success = databaseHelper.addBorrower(borrowerName, selectedBook, fromDate, toDate)
                if (success) {
                    Toast.makeText(this, "Book borrowed successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to borrow book", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDateTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()

        // Show DatePickerDialog
        DatePickerDialog(this, { _, year, month, dayOfMonth ->
            // Update calendar with selected date
            calendar.set(year, month, dayOfMonth)

            // Show TimePickerDialog after date is selected
            TimePickerDialog(this, { _, hourOfDay, minute ->
                // Update calendar with selected time
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, minute)

                // Format date and time to display in EditText
                val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                editText.setText(dateTimeFormat.format(calendar.time))
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }
}
