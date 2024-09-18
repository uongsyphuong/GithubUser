package com.example.githubuser.api

import com.example.githubuser.model.api.UserResponse
import com.example.githubuser.model.resource.Resource
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

object ApiPath {
    const val USERS = "/users"
    const val USER_DETAILS = "/users/{username}"
}

interface ApiService {

    @GET(ApiPath.USERS)
    fun getUserList(
        @Query("per_page") perPage: String,
        @Query("since") since: String,
    ): Flow<Resource<List<UserResponse>>>

    @GET(ApiPath.USER_DETAILS)
    fun getUserDetails(
        @Path("username") username: String
    ): Flow<Resource<UserResponse>>
}