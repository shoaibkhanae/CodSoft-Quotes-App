package com.example.quotesapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quotesapp.R
import com.example.quotesapp.data.model.Result

class MainAdapter : ListAdapter<Result,MainAdapter.MainViewHolder>(QuoteDiffCallBack()) {
    class MainViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var content: TextView = view.findViewById(R.id.tv_content)
        var author: TextView = view.findViewById(R.id.tv_author)

        fun onBind(quote: Result) {
            content.text = quote.content
            author.text = quote.author
        }

        companion object {
            fun create(parent: ViewGroup) : MainViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.quotes_item,parent,false)

                return MainViewHolder(view)
            }
        }
    }

    class QuoteDiffCallBack : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val current = getItem(position)
        holder.onBind(current)

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
    }
}