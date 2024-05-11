package com.example.quotesapp.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quotesapp.R
import com.example.quotesapp.data.model.Result

class SearchAdapter(private val dataset: List<Result>) : RecyclerView.Adapter<SearchAdapter.SearchViewHolder>() {
    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var content: TextView = view.findViewById(R.id.tv_content)
        var author: TextView = view.findViewById(R.id.tv_author)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.quotes_item,parent,false)
        return SearchViewHolder(view)
    }

    override fun getItemCount(): Int = dataset.size

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        val current = dataset[position]
        holder.content.text = current.content
        holder.author.text = current.author
    }
}