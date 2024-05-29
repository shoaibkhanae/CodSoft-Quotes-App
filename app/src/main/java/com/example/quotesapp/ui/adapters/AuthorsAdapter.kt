package com.example.quotesapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.quotesapp.R
import com.example.quotesapp.data.model.entities.Write

class AuthorsAdapter: ListAdapter<Write,AuthorsAdapter.AuthorsViewHolder>(AuthorsDiffCallBack()) {
    class AuthorsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var content: TextView = view.findViewById(R.id.tv_content)
        var author: TextView = view.findViewById(R.id.tv_author)

        fun onBind(write: Write) {
            content.text = write.content
            author.text = write.author
        }

        companion object {
            fun create(parent: ViewGroup) : AuthorsViewHolder {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.quotes_item,parent,false)

                return AuthorsViewHolder(view)
            }
        }
    }

    class AuthorsDiffCallBack : DiffUtil.ItemCallback<Write>() {
        override fun areItemsTheSame(oldItem: Write, newItem: Write): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Write, newItem: Write): Boolean {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorsViewHolder {
        return AuthorsViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: AuthorsViewHolder, position: Int) {
        val current = getItem(position)
        holder.onBind(current)

        holder.itemView.setOnClickListener {
            onItemClickListener?.onItemClick(position)
        }
    }
}