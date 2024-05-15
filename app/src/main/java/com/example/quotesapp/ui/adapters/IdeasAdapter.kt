package com.example.quotesapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quotesapp.R
import com.example.quotesapp.data.model.entities.Quote


class IdeasAdapter
    : ListAdapter<Quote, IdeasAdapter.IdeasViewHolder>(QuoteDiffCallBack()) {

    class IdeasViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var content: TextView = view.findViewById(R.id.tv_content)
        var author: TextView = view.findViewById(R.id.tv_author)

        fun onBind(quote: Quote) {
            content.text = quote.content
            author.text = quote.author
        }

        companion object {
            fun create(parent: ViewGroup) : IdeasViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.quotes_item,parent,false)

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

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IdeasViewHolder {
        return IdeasViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: IdeasViewHolder, position: Int) {
        val current = getItem(position)
        holder.onBind(current)

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
    }
}