package com.example.githubuser.viewmodel

import androidx.lifecycle.ViewModel
import com.example.githubuser.model.resource.Resource
import com.example.githubuser.model.resource.ResourceError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

open class BaseViewModel : ViewModel() {

    private val _generalError = MutableStateFlow<String?>(null)
    val generalError: StateFlow<String?> = _generalError.asStateFlow()

    private val _networkUnavailableEvent = MutableStateFlow(false)
    val networkUnavailableEvent: StateFlow<Boolean> = _networkUnavailableEvent.asStateFlow()

    fun <T> Flow<Resource<T>>.handleError(
        onError: ((ResourceError) -> Unit)? = null
    ): Flow<T?> = flow {
        collect { r ->
            when (r) {
                is Resource.Success -> {
                    emit(r.data)
                    _generalError.value = null
                }

                is Resource.Error -> {
                    if (r.error.type == ResourceError.ErrorType.NETWORK_UNAVAILABLE) {
                        _networkUnavailableEvent.value = true
                    } else {
                        _networkUnavailableEvent.value = false
                        onError?.invoke(r.error) ?: run {
                            _generalError.value = r.error.message
                        }
                    }

                }
            }
        }
    }
}