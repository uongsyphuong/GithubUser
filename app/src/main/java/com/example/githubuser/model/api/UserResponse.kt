package com.example.githubuser.model.api

import com.google.gson.annotations.SerializedName

data class UserResponse(
    @SerializedName("login") val username: String?,
    @SerializedName("avatar_url") val avatarUrl: String?,
    @SerializedName("html_url") val htmlUrl: String?,
    @SerializedName("location") val location: String?,
    @SerializedName("followers") val followers: Int?,
    @SerializedName("following") val following: Int?
)