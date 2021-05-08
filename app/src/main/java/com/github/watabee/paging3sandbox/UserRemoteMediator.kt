package com.github.watabee.paging3sandbox

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.github.watabee.paging3sandbox.data.User
import com.github.watabee.paging3sandbox.db.UserDao
import com.github.watabee.paging3sandbox.network.GitHubApi
import timber.log.Timber

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(private val userDao: UserDao, private val gitHubApi: GitHubApi) : RemoteMediator<Int, User>() {

    override suspend fun initialize(): InitializeAction {
        Timber.tag("UserRemoteMediator").d("initialize()")
        return super.initialize()
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        Timber.tag("UserRemoteMediator").d("load # loadType = $loadType")

        val loadKey = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                val lastUserId = userDao.getUserRemoteKey()
                Timber.tag("UserRemoteMediator").d("load # lastUserId = $lastUserId")
                if (lastUserId == null) {
                    return MediatorResult.Success(endOfPaginationReached = true)
                }
                lastUserId
            }
        }

        Timber.tag("UserRemoteMediator").d("loadKey = $loadKey")

        val isRefresh = loadType == LoadType.REFRESH
        val perPage = if (isRefresh) state.config.initialLoadSize else state.config.pageSize
        return try {
            val users = gitHubApi.getUsers(since = loadKey, perPage = perPage)
            userDao.update(users = users, lastUserId = users.lastOrNull()?.id, shouldClearData = isRefresh)

            MediatorResult.Success(endOfPaginationReached = false)
        } catch (e: Throwable) {
            Timber.tag("UserRemoteMediator").e(e)
            MediatorResult.Error(e)
        }
    }
}
