package com.github.watabee.paging3sandbox

import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.paging.insertFooterItem
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userAdapter = UserAdapter()
        val footerAdapter = FooterAdapter { userAdapter.retry() }
        val adapter = ConcatAdapter(
            ConcatAdapter.Config.Builder().setIsolateViewTypes(false).build(),
            userAdapter,
            footerAdapter
        )

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 3).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when (adapter.getItemViewType(position)) {
                        R.layout.list_item_footer -> spanCount
                        else -> 1
                    }
                }
            }
        }
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(object: RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val margin = resources.getDimensionPixelSize(R.dimen.dimen_8dp)
                outRect.set(margin, margin, margin, margin)
            }
        })

        val swipeRefreshLayout: SwipeRefreshLayout = findViewById(R.id.swipe_refresh_layout)
        swipeRefreshLayout.setOnRefreshListener { userAdapter.refresh() }

        val errorView: View = findViewById(R.id.error_view)
        val retryButton: Button = findViewById(R.id.retry_button)
        retryButton.setOnClickListener { userAdapter.retry() }

        lifecycleScope.launch {
            viewModel.pagingData.collectLatest(userAdapter::submitData)
        }

        lifecycleScope.launch {
            userAdapter.loadStateFlow.collectLatest { loadStates ->
                swipeRefreshLayout.isRefreshing = loadStates.refresh is LoadState.Loading
                errorView.isVisible = loadStates.refresh is LoadState.Error
                footerAdapter.loadState = loadStates.append
            }
        }
    }
}