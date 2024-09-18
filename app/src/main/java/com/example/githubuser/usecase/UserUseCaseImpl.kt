package com.example.githubuser.usecase

import com.example.githubuser.model.api.UserResponse
import com.example.githubuser.model.resource.Resource
import com.example.githubuser.repository.UserRepository
import com.example.githubuser.storage.AppDataStorage
import com.example.githubuser.ui.uistate.UserDetailsUiModel
import com.example.githubuser.ui.uistate.UserListUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserUseCaseImpl @Inject constructor(
    private val userRepository: UserRepository,
    private val appDataStorage: AppDataStorage
) : UserUseCase {

    private val userList = mutableListOf<UserResponse>()

    override fun getUserList(
        perPage: Int,
        since: Int
    ): Flow<Resource<UserListUiModel>> {
        val cachedUserList = appDataStorage.getUserListFromInternalStorage()
        if (cachedUserList != null && cachedUserList.size > since){
            userList.addAll(cachedUserList)
            return flowOf(
                Resource.Success(
                    UserListUiModel(
                        userListItem = userList.map { user ->
                            mapToUiModel(user)
                        },
                        isLoadMore = true
                    )
                )
            )
        } else {
            return userRepository.getUserList(perPage.toString(), since.toString())
                .map {
                    when (it) {
                        is Resource.Error -> Resource.Error(it.error)
                        is Resource.Success -> {
                            it.data?.takeIf { data -> data.isNotEmpty() }?.let { data ->
                                userList.addAll(data)
                                appDataStorage.storeUserListToInternalStorage(userList)
                            }

                            Resource.Success(
                                UserListUiModel(
                                    userListItem = userList.map { user ->
                                        mapToUiModel(user)
                                    },
                                    isLoadMore = (it.data?.size ?: 0) >= perPage
                                )
                            )
                        }
                    }
                }
        }


    }

    private fun mapToUiModel(userResponse: UserResponse): UserDetailsUiModel {
        return UserDetailsUiModel(
            username = userResponse.username ?: "",
            imageUrl = userResponse.avatarUrl ?: "",
            url = userResponse.htmlUrl ?: "",
            location = userResponse.location,
            followers = userResponse.followers ?: 0,
            following = userResponse.following ?: 0
        )
    }

    override fun getUserDetails(username: String): Flow<Resource<UserDetailsUiModel>> {
        return userRepository.getUserDetails(username).map {
            when (it) {
                is Resource.Error -> Resource.Error(it.error)
                is Resource.Success -> Resource.Success(it.data?.let { it1 -> mapToUiModel(it1) })
            }
        }
    }

}