package com.github.watabee.paging3sandbox.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.github.watabee.paging3sandbox.data.User

@Dao
abstract class UserDao {

    @Query("SELECT * FROM users")
    abstract fun findUsers(): PagingSource<Int, User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertAll(users: List<User>)

    @Query("DELETE FROM users")
    abstract suspend fun deleteAll()

    @Query("INSERT OR REPLACE INTO user_management (id, lastUserId, updatedAt) VALUES (1, :lastUserId, :updatedAt)")
    abstract suspend fun insertUserManagementData(lastUserId: Int?, updatedAt: Long)

    @Query("SELECT lastUserId FROM user_management WHERE id = 1")
    abstract suspend fun getLastUserId(): Int?

    @Query("SELECT updatedAt FROM user_management WHERE id = 1")
    abstract suspend fun getUpdatedAt(): Long?

    @Transaction
    open suspend fun update(users: List<User>, lastUserId: Int?, currentMillis: Long, isRefresh: Boolean) {
        if (isRefresh) {
            deleteAll()
        }
        insertAll(users)
        val updatedAt = if (isRefresh) currentMillis else getUpdatedAt() ?: 0
        insertUserManagementData(lastUserId, updatedAt)
    }
}
