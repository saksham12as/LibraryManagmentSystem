package com.example.librarymanagementsystem

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BorrowersAdapter(
    private val borrowerList: List<Borrower>,
    private val actionListener: BorrowerActionListener
) : RecyclerView.Adapter<BorrowersAdapter.BorrowerViewHolder>() {

    interface BorrowerActionListener {
        fun onEditBorrower(borrower: Borrower)
        fun onDeleteBorrower(borrower: Borrower)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BorrowerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_borrower, parent, false)
        return BorrowerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BorrowerViewHolder, position: Int) {
        val borrower = borrowerList[position]

        // Populate data into item views
        holder.borrowerName.text = borrower.name
        holder.borrowedBook.text = "Borrowed Book: ${borrower.borrowedBook}"
        holder.borrowDate.text = "Borrowed On: ${borrower.borrowDate}"
        holder.returnDate.text = "Return By: ${borrower.returnDate}"

        // Handle Edit action
        holder.editIcon.setOnClickListener {
            actionListener.onEditBorrower(borrower)
        }

        // Handle Delete action
        holder.deleteIcon.setOnClickListener {
            actionListener.onDeleteBorrower(borrower)
        }
    }

    override fun getItemCount(): Int = borrowerList.size

    inner class BorrowerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val borrowerName: TextView = view.findViewById(R.id.borrowerNameText)
        val borrowedBook: TextView = view.findViewById(R.id.borrowedBookText)
        val borrowDate: TextView = view.findViewById(R.id.borrowDateText)
        val returnDate: TextView = view.findViewById(R.id.returnDateText)
        val editIcon: ImageView = view.findViewById(R.id.editBorrowerIcon)
        val deleteIcon: ImageView = view.findViewById(R.id.deleteBorrowerIcon)
    }
}
