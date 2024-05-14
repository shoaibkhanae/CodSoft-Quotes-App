package com.example.quotesapp.data.paging.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.quotesapp.R
import com.example.quotesapp.data.model.Result
import com.example.quotesapp.ui.fragments.HomeFragmentDirections
import com.google.android.material.card.MaterialCardView

class QuotePagingAdapter:
    PagingDataAdapter<Result, QuotePagingAdapter.QuoteViewHolder>(ItemComparator()) {

    class QuoteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var content: TextView = view.findViewById(R.id.tv_content)
        var author: TextView = view.findViewById(R.id.tv_author)
        var card: MaterialCardView = view.findViewById(R.id.quote_card)

        fun onBind(quote: Result) {
            content.text = quote.content
            author.text = quote.author
        }

        companion object {
            fun create(parent: ViewGroup) : QuoteViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.quotes_item,parent,false)

                return QuoteViewHolder(view)
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.content == newItem.content
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteViewHolder {
        return QuoteViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: QuoteViewHolder, position: Int) {
        val current = getItem(position)
        if (current != null) {
            holder.onBind(current)

            holder.card.setOnClickListener {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToQuoteFragment(current.content,current.author)
                holder.itemView.findNavController().navigate(action)
            }
        }
    }
}