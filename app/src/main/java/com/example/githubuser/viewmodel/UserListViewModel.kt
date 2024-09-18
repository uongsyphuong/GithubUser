package com.example.githubuser.viewmodel

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
class UserListViewModel @Inject constructor(
    private val userUseCase: UserUseCase
) : BaseViewModel() {
    private var paginationOffSet = 0
    var canPaginate = false

    private val _userList = MutableStateFlow<List<UserDetailsUiModel>>(emptyList())
    val userList: StateFlow<List<UserDetailsUiModel>> = _userList.asStateFlow()

    fun getUserList(resetList: Boolean = false) {
        viewModelScope.launch(Dispatchers.Main) {
            if (resetList) {
                paginationOffSet = 0
                canPaginate = false
                _userList.value = emptyList()

            }
            userUseCase.getUserList(
                perPage = PAGE_SIZE, since = paginationOffSet
            ).handleError().collect { userListUiModel ->
                    if (userListUiModel != null) {
                        _userList.value = userListUiModel.userListItem
                        canPaginate = userListUiModel.isLoadMore
                        if (userListUiModel.isLoadMore) {
                            paginationOffSet = _userList.value.size
                        }
                    }
                }
        }
    }

    companion object {
        private const val PAGE_SIZE = 20
    }
}