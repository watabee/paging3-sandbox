package com.github.watabee.paging3sandbox

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.github.watabee.paging3sandbox.data.User
import com.github.watabee.paging3sandbox.db.UserDao
import com.github.watabee.paging3sandbox.network.GitHubApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(userDao: UserDao, gitHubApi: GitHubApi) : ViewModel() {

    @OptIn(ExperimentalPagingApi::class)
    val pagingData: Flow<PagingData<User>> =
        Pager(
            config = PagingConfig(pageSize = 30),
            initialKey = 0,
            remoteMediator = UserRemoteMediator(userDao, gitHubApi),
            pagingSourceFactory = { userDao.findUsers() }
        ).flow.cachedIn(viewModelScope)
}