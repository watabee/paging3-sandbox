package com.github.watabee.paging3sandbox

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class LoadingAdapter : RecyclerView.Adapter<LoadingViewHolder>() {
    var showLoading: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                notifyDataSetChanged()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoadingViewHolder = LoadingViewHolder(parent)

    override fun onBindViewHolder(holder: LoadingViewHolder, position: Int) {
        // do nothing.
    }

    override fun getItemCount(): Int = if (showLoading) 1 else 0
}

class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item_loading, parent, false)
)
