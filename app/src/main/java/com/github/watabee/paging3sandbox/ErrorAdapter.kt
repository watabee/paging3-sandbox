package com.github.watabee.paging3sandbox

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class ErrorAdapter(private val onRetryButtonClicked: () -> Unit) : RecyclerView.Adapter<ErrorViewHolder>() {
    var showError: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ErrorViewHolder = ErrorViewHolder(parent, onRetryButtonClicked)

    override fun onBindViewHolder(holder: ErrorViewHolder, position: Int) {
        // do nothing.
    }

    override fun getItemViewType(position: Int): Int = R.layout.list_item_error

    override fun getItemCount(): Int = if (showError) 1 else 0
}

class ErrorViewHolder(parent: ViewGroup, onRetryButtonClicked: () -> Unit) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item_error, parent, false)
) {
    init {
        val retryButton = itemView.findViewById<Button>(R.id.retry_button)
        retryButton.setOnClickListener { onRetryButtonClicked() }
    }
}
