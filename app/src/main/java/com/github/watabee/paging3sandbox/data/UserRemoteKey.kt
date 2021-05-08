package com.github.watabee.paging3sandbox.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_remote_keys")
data class UserRemoteKey(
    @PrimaryKey val id: Int,
    val lastUserId: Int?
)
