package com.example.quotesapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quotesapp.R
import com.example.quotesapp.data.db.Quote
import com.example.quotesapp.ui.viewmodels.QuoteViewModel
import com.google.android.material.snackbar.Snackbar

class IdeasAdapter(private val viewModel: QuoteViewModel)
    : ListAdapter<Quote, IdeasAdapter.IdeasViewHolder>(QuoteDiffCallBack()) {

    class IdeasViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var content: TextView = view.findViewById(R.id.saved_content)
        var author: TextView = view.findViewById(R.id.saved_author)
        var delete: ImageView = view.findViewById(R.id.delete_button)

        fun onBind(quote: Quote) {
            content.text = quote.content
            author.text = quote.author
        }

        companion object {
            fun create(parent: ViewGroup) : IdeasViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.saved_quote,parent,false)

                return IdeasViewHolder(view)
            }
        }
    }

    class QuoteDiffCallBack : DiffUtil.ItemCallback<Quote>() {
        override fun areItemsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Quote, newItem: Quote): Boolean {
            return oldItem.content == newItem.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeasViewHolder {
        return IdeasViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: IdeasViewHolder, position: Int) {
        val current = getItem(position)
        holder.onBind(current)

        holder.delete.setOnClickListener {
            viewModel.delete(current)

            Snackbar.make(it,"Quote deleted.", Snackbar.LENGTH_SHORT).apply {
                setAction("Undo") {
                    viewModel.insert(current)
                }
            }.show()
        }
    }
}