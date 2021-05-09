package com.github.watabee.paging3sandbox

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.progressindicator.CircularProgressIndicator

class FooterAdapter(private val onRetryButtonClicked: () -> Unit) : LoadStateAdapter<FooterViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): FooterViewHolder =
        FooterViewHolder(parent, onRetryButtonClicked)

    override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun getStateViewType(loadState: LoadState): Int = R.layout.list_item_footer
}

class FooterViewHolder(parent: ViewGroup, onRetryButtonClicked: () -> Unit) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.list_item_footer, parent, false)
) {
    private val retryButton = itemView.findViewById<Button>(R.id.retry_button)
    private val progressIndicator = itemView.findViewById<CircularProgressIndicator>(R.id.progress_indicator)

    init {
        retryButton.setOnClickListener { onRetryButtonClicked() }
    }

    fun bind(state: LoadState) {
        progressIndicator.isVisible = state == LoadState.Loading
        retryButton.isVisible = state is LoadState.Error
    }
}
