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

    @Query("INSERT OR REPLACE INTO user_remote_keys (id, lastUserId) VALUES (1, :lastUserId)")
    abstract suspend fun insertUserRemoteKey(lastUserId: Int)

    @Query("DELETE FROM user_remote_keys")
    abstract suspend fun deleteUserRemoteKey()

    @Query("SELECT lastUserId FROM user_remote_keys WHERE id = 1")
    abstract suspend fun getUserRemoteKey(): Int?

    @Transaction
    open suspend fun update(users: List<User>, lastUserId: Int?, shouldClearData: Boolean) {
        if (shouldClearData) {
            deleteAll()
        }
        insertAll(users)
        if (lastUserId != null) {
            insertUserRemoteKey(lastUserId)
        } else {
            deleteUserRemoteKey()
        }
    }
}
