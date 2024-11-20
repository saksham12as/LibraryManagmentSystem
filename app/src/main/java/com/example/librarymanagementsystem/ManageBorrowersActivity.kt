package com.example.librarymanagementsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ManageBorrowersActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BorrowersAdapter
    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_borrowers)

        // Initialize the database helper
        databaseHelper = DatabaseHelper(this)

        // Setup RecyclerView
        recyclerView = findViewById(R.id.borrowersRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load and display borrowers
        loadBorrowers()
    }

    private fun loadBorrowers() {
        val borrowerList = databaseHelper.getAllBorrowers()
        if (borrowerList.isNotEmpty()) {
            adapter = BorrowersAdapter(borrowerList, object : BorrowersAdapter.BorrowerActionListener {
                override fun onEditBorrower(borrower: Borrower) {
                    // Redirect to BorrowBookActivity for editing
                    val intent = Intent(this@ManageBorrowersActivity, BorrowBookActivity::class.java)
                    intent.putExtra("BORROWER_ID", borrower.id)
                    intent.putExtra("BORROWER_NAME", borrower.name)
                    intent.putExtra("BORROWED_BOOK", borrower.borrowedBook)
                    intent.putExtra("BORROW_DATE", borrower.borrowDate)
                    intent.putExtra("RETURN_DATE", borrower.returnDate)
                    startActivity(intent)
                }

                override fun onDeleteBorrower(borrower: Borrower) {
                    // Delete borrower
                    val isDeleted = databaseHelper.deleteBorrower(borrower.id)
                    if (isDeleted) {
                        Toast.makeText(this@ManageBorrowersActivity, "Borrower deleted successfully", Toast.LENGTH_SHORT).show()
                        loadBorrowers()
                    } else {
                        Toast.makeText(this@ManageBorrowersActivity, "Failed to delete borrower", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            recyclerView.adapter = adapter
            recyclerView.visibility = RecyclerView.VISIBLE
        } else {
            recyclerView.visibility = RecyclerView.GONE
            Toast.makeText(this, "No borrowers found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        loadBorrowers() // Refresh borrower list when returning to this activity
    }
}
