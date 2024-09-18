package com.example.githubuser.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.githubuser.ui.activity.UserDetailsActivity.Companion.USERNAME
import com.example.githubuser.ui.component.SimpleActionBar
import com.example.githubuser.ui.component.UserCard
import com.example.githubuser.ui.uistate.UserDetailsUiModel
import com.example.githubuser.viewmodel.UserListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserListActivity : BaseActivity() {

    override val viewModel: UserListViewModel by viewModels()

    @Composable
    override fun ComposeContent() {

        val userList by viewModel.userList.collectAsStateWithLifecycle()

        LaunchedEffect(Unit) {
            viewModel.getUserList(true)
        }

        Column {
            SimpleActionBar(
                title = "Github User"
            )
            UserList(userList)
        }

    }

    @Composable
    private fun UserList(
        userList: List<UserDetailsUiModel>
    ) {
        val lazyColumnListState = rememberLazyListState()
        val shouldStartPaginate = remember {
            derivedStateOf {
                val layoutInfo = lazyColumnListState.layoutInfo
                val totalItemsNumber = layoutInfo.totalItemsCount
                val lastVisibleItemIndex =
                    (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

                viewModel.canPaginate && lastVisibleItemIndex > (totalItemsNumber - BUFFER)
            }
        }

        LaunchedEffect(shouldStartPaginate.value) {
            if (shouldStartPaginate.value)
                viewModel.getUserList(false)
        }
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            state = lazyColumnListState
        ) {
            if (userList.isNotEmpty()) {
                items(userList) { item ->
                    val context = LocalContext.current
                    UserCard(uiModel = item) { username ->
                        val intent = Intent(context, UserDetailsActivity::class.java)
                        intent.putExtra(USERNAME, username)
                        startActivity(intent)
                    }
                }
            } else {
//                item {
//                    EmptyView(stringResource(id = R.string.earning_no_result))
//                }
            }
        }
    }


    @Preview(showBackground = true)
    @Composable
    fun ScreenPreview() {
        SimpleActionBar(
            title = "User List"
        )
        UserList(
            listOf()
        )
    }

    companion object {
        private const val BUFFER = 5
    }

}