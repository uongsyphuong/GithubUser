/*
 * Copyright (c) 2019 SP Group. All rights reserved.
 */

package com.example.githubuser.api

import com.example.githubuser.model.resource.Resource
import com.example.githubuser.model.resource.ResourceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.net.SocketTimeoutException
import kotlin.coroutines.resume

class ResourceFlowCallAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (Flow::class.java != getRawType(returnType)) {
            return null
        }
        if (returnType !is ParameterizedType) {
            throw IllegalStateException(
                "Flow return type must be parameterized as Flow<Foo> or Flow<out Foo>"
            )
        }
        val resourceType = getParameterUpperBound(0, returnType)
        if (Resource::class.java != getRawType(resourceType)) {
            return null
        }
        return ResourceCallAdapter<Any>(
            getParameterUpperBound(
                0,
                resourceType as ParameterizedType
            )
        )
    }

    private inner class ResourceCallAdapter<T>(private val responseType: Type) :
        CallAdapter<T, Flow<Resource<T>>> {
        private val ignoreEmptyBody = responseType == Unit.javaClass

        override fun responseType() = responseType

        override fun adapt(call: Call<T>): Flow<Resource<T>> {
            return flow {
                emit(
                    suspendCancellableCoroutine { continuation ->
                        call.enqueue(object : Callback<T> {
                            override fun onFailure(call: Call<T>, t: Throwable) {
                                if (!call.isCanceled) {
                                    continuation.resume(
                                        Resource.Error(
                                            parseApiException(t)
                                        )
                                    )
                                }
                            }

                            override fun onResponse(call: Call<T>, response: Response<T>) {
                                continuation.resume(handleResponse(call, response))
                            }
                        })
                        continuation.invokeOnCancellation { call.cancel() }
                    }
                )
            }
        }

        private fun <T> handleResponse(
            call: Call<T>,
            response: Response<T>
        ): Resource<T> {
            if (response.isSuccessful) {
                val body = response.body()
                return when {
                    body != null -> return Resource.Success(body)
                    ignoreEmptyBody -> Resource.Success(null)
                    else -> {
                        call.request()
                        Resource.Error(
                            ResourceError()
                        )
                    }
                }
            }

            return Resource.Error(
                ResourceError()
            )
        }

    }

    companion object {
        @JvmStatic
        @JvmName("create")
        operator fun invoke() = ResourceFlowCallAdapterFactory()
    }

    fun parseApiException(t: Throwable?): ResourceError {
        val e: ResourceError = when (t) {
            is NetworkUnavailableException -> {
                ResourceError(
                    ResourceError.ErrorType.NETWORK_UNAVAILABLE
                )
            }

            is SocketTimeoutException -> {
                ResourceError(
                    ResourceError.ErrorType.TIMEOUT
                )
            }

            else -> {
                ResourceError(
                    ResourceError.ErrorType.GENERIC
                )
            }
        }
        return e
    }
}
