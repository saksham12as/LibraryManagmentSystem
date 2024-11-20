package com.example.librarymanagementsystem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BooksAdapter(
    private val books: List<Book>,
    private val context: Context,
    private val onEdit: (Book) -> Unit,
    private val onDelete: (Book) -> Unit
) : RecyclerView.Adapter<BooksAdapter.BookViewHolder>() {

    inner class BookViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val bookImage: ImageView = view.findViewById(R.id.bookImage)
        val bookNameText: TextView = view.findViewById(R.id.bookNameText)
        val authorNameText: TextView = view.findViewById(R.id.authorNameText)
        val publishedDateText: TextView = view.findViewById(R.id.publishedDateText)
        val editIcon: ImageView = view.findViewById(R.id.editIcon)
        val deleteIcon: ImageView = view.findViewById(R.id.deleteIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.book_item, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]
        holder.bookNameText.text = book.bookName
        holder.authorNameText.text = "Author: ${book.authorName}"
        holder.publishedDateText.text = "Published: ${book.publishedDate}"

        // Placeholder for book image
        holder.bookImage.setImageResource(R.drawable.ic_book_placeholder)

        // Edit action
        holder.editIcon.setOnClickListener {
            onEdit(book) // Pass the book to the edit handler
        }

        // Delete action
        holder.deleteIcon.setOnClickListener {
            onDelete(book) // Pass the book to the delete handler
        }
    }

    override fun getItemCount(): Int = books.size
}
