package com.example.githubuser.ui.uistate

data class UserDetailsUiModel(
    val username: String = "",
    val imageUrl: String = "",
    val url: String = "",
    val location: String? = null,
    val followers: Int = 0,
    val following: Int = 0
)