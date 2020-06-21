package com.github.watabee.paging3sandbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.flow.Flow

class MainViewModel : ViewModel() {

    val pagingData: Flow<PagingData<String>> =
        Pager(
            config = PagingConfig(20, prefetchDistance = 10, initialLoadSize = 20),
            initialKey = 0,
            remoteMediator = null,
            pagingSourceFactory = { MyPagingSource() }
        ).flow.cachedIn(viewModelScope)
}