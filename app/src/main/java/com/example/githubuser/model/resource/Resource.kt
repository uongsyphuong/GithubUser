package com.example.githubuser.model.resource

sealed class Resource<out T> {
    data class Success<T>(val data: T?) : Resource<T>()
    data class Error(val error: ResourceError) : Resource<Nothing>()
}