package com.github.watabee.paging3sandbox

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.github.watabee.paging3sandbox.data.User
import com.github.watabee.paging3sandbox.db.UserDao
import com.github.watabee.paging3sandbox.network.GitHubApi
import timber.log.Timber

private const val CACHE_MILLIS = 5 * 60 * 1000L // 5min.

@OptIn(ExperimentalPagingApi::class)
class UserRemoteMediator(private val userDao: UserDao, private val gitHubApi: GitHubApi) : RemoteMediator<Int, User>() {

    override suspend fun initialize(): InitializeAction {
        val now = System.currentTimeMillis()
        val updatedAt = userDao.getUpdatedAt() ?: 0
        val diff = now - updatedAt
        Timber.tag("UserRemoteMediator").d("initialize(), diff = $diff, CACHE_MILLIS = $CACHE_MILLIS")
        return if (diff >= CACHE_MILLIS) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(loadType: LoadType, state: PagingState<Int, User>): MediatorResult {
        Timber.tag("UserRemoteMediator").d("load # loadType = $loadType")

        val loadKey = when (loadType) {
            LoadType.REFRESH -> null
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                userDao.getLastUserId() ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
        }

        Timber.tag("UserRemoteMediator").d("loadKey = $loadKey")

        val isRefresh = loadType == LoadType.REFRESH
        val perPage = if (isRefresh) state.config.initialLoadSize else state.config.pageSize
        return try {
            val users = gitHubApi.getUsers(since = loadKey, perPage = perPage)
            userDao.update(
                users = users,
                lastUserId = users.lastOrNull()?.id,
                currentMillis = System.currentTimeMillis(),
                isRefresh = isRefresh
            )

            MediatorResult.Success(endOfPaginationReached = false)
        } catch (e: Throwable) {
            Timber.tag("UserRemoteMediator").e(e)
            MediatorResult.Error(e)
        }
    }
}
