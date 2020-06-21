package com.github.watabee.paging3sandbox

import androidx.paging.PagingSource
import kotlinx.coroutines.delay
import timber.log.Timber

class MyPagingSource : PagingSource<Int, String>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, String> {
        Timber.tag("MyPagingSource").d("load # key = ${params.key}")

        delay(1000L)

        val key = params.key ?: 0
        return LoadResult.Page(
            data = (0 until params.loadSize).map { "[MyPagingSource] $key-$it" },
            prevKey = if (key == 0) null else key - 1,
            nextKey = if (key < 10) key + 1 else null
        )
    }
}