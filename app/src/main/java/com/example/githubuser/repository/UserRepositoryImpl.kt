package com.example.githubuser.repository

import com.example.githubuser.api.ApiService
import com.example.githubuser.model.api.UserResponse
import com.example.githubuser.model.resource.Resource
import kotlinx.coroutines.flow.Flow

class UserRepositoryImpl(
    private val apiService: ApiService
): UserRepository  {


    override fun getUserList(perPage: String, since: String): Flow<Resource<List<UserResponse>>> {
        return apiService.getUserList(perPage,since)
    }

    override fun getUserDetails(username: String): Flow<Resource<UserResponse>> {
        return apiService.getUserDetails(username)
    }
}