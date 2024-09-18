package com.example.githubuser.usecase

import com.example.githubuser.model.resource.Resource
import com.example.githubuser.ui.uistate.UserDetailsUiModel
import com.example.githubuser.ui.uistate.UserListUiModel
import kotlinx.coroutines.flow.Flow

interface UserUseCase {
    fun getUserList(perPage: Int, since: Int): Flow<Resource<UserListUiModel>>

    fun getUserDetails(username: String): Flow<Resource<UserDetailsUiModel>>
}