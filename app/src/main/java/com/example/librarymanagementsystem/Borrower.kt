package com.example.librarymanagementsystem

data class Borrower(
    val id: Int,            // Unique ID of the borrower
    val name: String,       // Borrower's name
    val borrowedBook: String, // Title of the borrowed book
    val borrowDate: String, // Borrow start date
    val returnDate: String  // Borrow return date
)
