package com.example.librarymanagementsystem

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ViewBooksActivity : AppCompatActivity() {

    private lateinit var databaseHelper: DatabaseHelper
    private lateinit var adapter: BooksAdapter
    private lateinit var booksRecyclerView: RecyclerView
    private var books = mutableListOf<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_books)

        // Initialize database helper
        databaseHelper = DatabaseHelper(this)

        // Initialize RecyclerView and set layout manager
        booksRecyclerView = findViewById(R.id.booksRecyclerView)
        booksRecyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize adapter
        adapter = BooksAdapter(
            books = books,
            context = this,
            onEdit = { book ->
                val intent = Intent(this, AddBookActivity::class.java).apply {
                    putExtra("BOOK_ID", book.bookId)
                    putExtra("BOOK_NAME", book.bookName)
                    putExtra("AUTHOR_NAME", book.authorName)
                    putExtra("PUBLISHED_DATE", book.publishedDate)
                }
                startActivityForResult(intent, EDIT_BOOK_REQUEST_CODE)
            },
            onDelete = { book ->
                val success = databaseHelper.deleteBook(book.bookId)
                if (success) {
                    Toast.makeText(this, "Book deleted successfully", Toast.LENGTH_SHORT).show()
                    loadBooks() // Refresh the list after deletion
                } else {
                    Toast.makeText(this, "Error deleting book", Toast.LENGTH_SHORT).show()
                }
            }
        )
        booksRecyclerView.adapter = adapter

        // Load books from database
        loadBooks()
    }

    private fun loadBooks() {
        books.clear()
        books.addAll(databaseHelper.getAllBooks())
        adapter.notifyDataSetChanged() // Notify adapter of data changes
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == EDIT_BOOK_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Refresh the list after editing
            loadBooks()
        }
    }

    companion object {
        const val EDIT_BOOK_REQUEST_CODE = 100
    }
}
