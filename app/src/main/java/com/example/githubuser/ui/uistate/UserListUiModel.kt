package com.example.githubuser.ui.uistate

data class UserListUiModel(
    val userListItem: List<UserDetailsUiModel> = listOf(),
    val isLoadMore: Boolean = false
)
