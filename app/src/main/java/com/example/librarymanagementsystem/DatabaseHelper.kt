package com.example.librarymanagementsystem

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        // Create Books Table
        val createBooksTable = ("CREATE TABLE $TABLE_BOOKS (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_BOOK_ID TEXT UNIQUE, " +
                "$COLUMN_BOOK_NAME TEXT NOT NULL, " +
                "$COLUMN_AUTHOR_NAME TEXT NOT NULL, " +
                "$COLUMN_PUBLISHED_DATE TEXT NOT NULL)")
        db.execSQL(createBooksTable)

        // Create Borrowers Table
        val createBorrowersTable = ("CREATE TABLE $TABLE_BORROWERS (" +
                "$COLUMN_BORROWER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_BORROWER_NAME TEXT NOT NULL, " +
                "$COLUMN_BORROWED_BOOK TEXT NOT NULL, " +
                "$COLUMN_BORROW_DATE TEXT NOT NULL, " +
                "$COLUMN_RETURN_DATE TEXT NOT NULL)")
        db.execSQL(createBorrowersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_BORROWERS")
        onCreate(db)
    }

    // CRUD Operations for Books
    fun addBook(bookName: String, bookId: String, authorName: String, publishedDate: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BOOK_NAME, bookName)
            put(COLUMN_BOOK_ID, bookId)
            put(COLUMN_AUTHOR_NAME, authorName)
            put(COLUMN_PUBLISHED_DATE, publishedDate)
        }
        val result = db.insert(TABLE_BOOKS, null, values)
        db.close()
        return result != -1L
    }

    fun getAllBooks(): List<Book> {
        val bookList = mutableListOf<Book>()
        val selectQuery = "SELECT * FROM $TABLE_BOOKS ORDER BY $COLUMN_BOOK_NAME ASC"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val book = Book(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    bookId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID)),
                    bookName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_NAME)),
                    authorName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR_NAME)),
                    publishedDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PUBLISHED_DATE))
                )
                bookList.add(book)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return bookList
    }

    fun deleteBook(bookId: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_BOOKS, "$COLUMN_BOOK_ID=?", arrayOf(bookId))
        db.close()
        return result > 0
    }

    fun updateBook(bookId: String, newBookName: String, newAuthorName: String, newPublishedDate: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BOOK_NAME, newBookName)
            put(COLUMN_AUTHOR_NAME, newAuthorName)
            put(COLUMN_PUBLISHED_DATE, newPublishedDate)
        }
        val result = db.update(TABLE_BOOKS, values, "$COLUMN_BOOK_ID=?", arrayOf(bookId))
        db.close()
        return result > 0
    }

    // CRUD Operations for Borrowers
    fun addBorrower(borrowerName: String, borrowedBook: String, borrowDate: String, returnDate: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BORROWER_NAME, borrowerName)
            put(COLUMN_BORROWED_BOOK, borrowedBook)
            put(COLUMN_BORROW_DATE, borrowDate)
            put(COLUMN_RETURN_DATE, returnDate)
        }
        val result = db.insert(TABLE_BORROWERS, null, values)
        db.close()
        return result != -1L
    }

    fun getAllBorrowers(): List<Borrower> {
        val borrowerList = mutableListOf<Borrower>()
        val selectQuery = "SELECT * FROM $TABLE_BORROWERS ORDER BY $COLUMN_BORROWER_NAME ASC"
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(selectQuery, null)

        if (cursor.moveToFirst()) {
            do {
                val borrower = Borrower(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BORROWER_ID)),
                    name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BORROWER_NAME)),
                    borrowedBook = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BORROWED_BOOK)),
                    borrowDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BORROW_DATE)),
                    returnDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_RETURN_DATE))
                )
                borrowerList.add(borrower)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return borrowerList
    }

    fun deleteBorrower(borrowerId: Int): Boolean {
        val db = this.writableDatabase
        val result = db.delete(TABLE_BORROWERS, "$COLUMN_BORROWER_ID=?", arrayOf(borrowerId.toString()))
        db.close()
        return result > 0
    }

    fun updateBorrower(borrowerId: Int, newBorrowerName: String, newBorrowedBook: String, newBorrowDate: String, newReturnDate: String): Boolean {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BORROWER_NAME, newBorrowerName)
            put(COLUMN_BORROWED_BOOK, newBorrowedBook)
            put(COLUMN_BORROW_DATE, newBorrowDate)
            put(COLUMN_RETURN_DATE, newReturnDate)
        }
        val result = db.update(TABLE_BORROWERS, values, "$COLUMN_BORROWER_ID=?", arrayOf(borrowerId.toString()))
        db.close()
        return result > 0
    }

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "LibraryDB"

        // Books Table
        const val TABLE_BOOKS = "Books"
        const val COLUMN_ID = "id"
        const val COLUMN_BOOK_ID = "bookId"
        const val COLUMN_BOOK_NAME = "bookName"
        const val COLUMN_AUTHOR_NAME = "authorName"
        const val COLUMN_PUBLISHED_DATE = "publishedDate"

        // Borrowers Table
        const val TABLE_BORROWERS = "Borrowers"
        const val COLUMN_BORROWER_ID = "id"
        const val COLUMN_BORROWER_NAME = "name"
        const val COLUMN_BORROWED_BOOK = "borrowedBook"
        const val COLUMN_BORROW_DATE = "borrowDate"
        const val COLUMN_RETURN_DATE = "returnDate"
    }
}
