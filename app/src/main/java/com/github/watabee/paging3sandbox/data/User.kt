package com.github.watabee.paging3sandbox.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: Int,
    @Json(name = "login") val name: String,
    @Json(name = "avatar_url") val avatarUrl: String
)
