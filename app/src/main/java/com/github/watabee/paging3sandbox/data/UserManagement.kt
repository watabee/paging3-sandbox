package com.github.watabee.paging3sandbox.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_management")
data class UserManagement(
    @PrimaryKey val id: Int,
    val lastUserId: Int?,
    val updatedAt: Long
)
