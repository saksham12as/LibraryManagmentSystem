package com.example.librarymanagementsystem

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Button to navigate to AddBookActivity
        findViewById<Button>(R.id.addBookButton).setOnClickListener {
            startActivity(Intent(this, AddBookActivity::class.java))
        }

        // Button to navigate to ViewBooksActivity
        findViewById<Button>(R.id.viewBooksButton).setOnClickListener {
            startActivity(Intent(this, ViewBooksActivity::class.java))
        }

        // Button to navigate to ManageBorrowersActivity
        findViewById<Button>(R.id.manageBorrowersButton).setOnClickListener {
            startActivity(Intent(this, ManageBorrowersActivity::class.java))
        }

        // Button to navigate to BorrowBookActivity
        findViewById<Button>(R.id.borrowBookButton).setOnClickListener {
            startActivity(Intent(this, BorrowBookActivity::class.java))
        }
    }
}
