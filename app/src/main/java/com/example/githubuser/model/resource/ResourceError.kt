package com.example.githubuser.model.resource

class ResourceError @JvmOverloads constructor(
    val type: ErrorType = ErrorType.GENERIC,
    val message: String? = null,
) {

    enum class ErrorType {
        GENERIC,
        TIMEOUT,
        NETWORK_UNAVAILABLE,
    }
}
