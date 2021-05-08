package com.github.watabee.paging3sandbox.network

import com.github.watabee.paging3sandbox.data.User
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApi {

    @GET("users")
    suspend fun getUsers(@Query("since") since: Int? = null, @Query("per_page") perPage: Int? = null): List<User>
}
