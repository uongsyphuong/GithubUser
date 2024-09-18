package com.example.githubuser.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubuser.ui.uistate.UserDetailsUiModel
import com.example.githubuser.usecase.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val userUseCase: UserUseCase
): BaseViewModel() {

    private val _userDetails = MutableStateFlow(UserDetailsUiModel())
    val userDetails: StateFlow<UserDetailsUiModel> = _userDetails.asStateFlow()

    fun getUserDetails(username: String) {
        viewModelScope.launch(Dispatchers.Main){
            userUseCase.getUserDetails(username)
                .handleError()
                .collect { userDetailsUiModel ->
                    if (userDetailsUiModel != null) {
                        _userDetails.value = userDetailsUiModel
                    }
                }
        }
    }
}