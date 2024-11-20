package com.example.librarymanagementsystem

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddBookActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private var isEditMode = false
    private var bookIdToEdit: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_book)

        // Initialize the database helper
        databaseHelper = DatabaseHelper(this)

        val bookNameInput = findViewById<EditText>(R.id.bookNameInput)
        val bookIdInput = findViewById<EditText>(R.id.bookIdInput)
        val authorNameInput = findViewById<EditText>(R.id.authorNameInput)
        val publishedDateInput = findViewById<EditText>(R.id.publishedDateInput)
        val saveButton = findViewById<Button>(R.id.saveBookButton)

        // Check if the activity is in edit mode
        intent?.let {
            val bookId = it.getStringExtra("BOOK_ID")
            val bookName = it.getStringExtra("BOOK_NAME")
            val authorName = it.getStringExtra("AUTHOR_NAME")
            val publishedDate = it.getStringExtra("PUBLISHED_DATE")

            if (bookId != null && bookName != null && authorName != null && publishedDate != null) {
                isEditMode = true
                bookIdToEdit = bookId

                bookIdInput.setText(bookId)
                bookIdInput.isEnabled = false // Disable editing the book ID in edit mode
                bookNameInput.setText(bookName)
                authorNameInput.setText(authorName)
                publishedDateInput.setText(publishedDate)
                saveButton.text = "Update Book" // Change button text in edit mode
            }
        }

        saveButton.setOnClickListener {
            val bookName = bookNameInput.text.toString().trim()
            val bookId = bookIdInput.text.toString().trim()
            val authorName = authorNameInput.text.toString().trim()
            val publishedDate = publishedDateInput.text.toString().trim()

            // Validate inputs
            if (bookName.isEmpty() || bookId.isEmpty() || authorName.isEmpty() || publishedDate.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (isEditMode) {
                // Update existing book
                val success = databaseHelper.updateBook(bookId, bookName, authorName, publishedDate)
                if (success) {
                    Toast.makeText(this, "Book updated successfully", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK) // Indicate success to parent activity
                    finish()
                } else {
                    Toast.makeText(this, "Error updating book", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Add a new book
                val success = databaseHelper.addBook(bookName, bookId, authorName, publishedDate)
                if (success) {
                    Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK) // Indicate success to parent activity
                    finish()
                } else {
                    Toast.makeText(this, "Error adding book. Make sure the Book ID is unique.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
