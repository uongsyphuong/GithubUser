package com.example.githubuser.api

import com.example.githubuser.utils.NetworkHelper
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class NetworkAvailabilityInterceptor(private val networkHelper: NetworkHelper) : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!networkHelper.isConnected()) {
            throw NetworkUnavailableException()
        }
        return chain.proceed(chain.request())
    }
}

class NetworkUnavailableException : IOException()

