package com.example.githubuser.repository

import com.example.githubuser.model.api.UserResponse
import com.example.githubuser.model.resource.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserList(perPage: String, since: String): Flow<Resource<List<UserResponse>>>

    fun getUserDetails(username: String): Flow<Resource<UserResponse>>
}