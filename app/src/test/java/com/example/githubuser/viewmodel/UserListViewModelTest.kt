package com.example.githubuser.viewmodel

import com.example.githubuser.model.resource.Resource
import com.example.githubuser.model.resource.ResourceError
import com.example.githubuser.ui.uistate.UserDetailsUiModel
import com.example.githubuser.ui.uistate.UserListUiModel
import com.example.githubuser.usecase.UserUseCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

@ExperimentalCoroutinesApi
class UserListViewModelTest : BaseViewModelTestCase<UserListViewModel>() {

    @MockK
    private lateinit var userUseCase: UserUseCase

    override fun newViewModel(): UserListViewModel {
        return UserListViewModel(userUseCase)
    }

    @Test
    fun `test getUserList have data`() = runTest {
        every {
            userUseCase.getUserList(any(), any())
        }.returns(
            flowOf(
                Resource.Success(
                    UserListUiModel(
                        listOf(
                            getUserDetailsUiModel("user1"),
                            getUserDetailsUiModel("user2"),
                            getUserDetailsUiModel("user3"),
                            getUserDetailsUiModel("user4"),
                            getUserDetailsUiModel("user5")
                        ),
                        isLoadMore = true
                    )
                )
            )
        )
        val viewModel = newViewModel()
        viewModel.getUserList(true)
        advanceUntilIdle()

        assertEquals(5, viewModel.userList.value.size)
        assertTrue(viewModel.canPaginate)

        every {
            userUseCase.getUserList(any(), any())
        }.returns(
            flowOf(
                Resource.Success(
                    UserListUiModel(
                        listOf(
                            getUserDetailsUiModel("user1"),
                            getUserDetailsUiModel("user2"),
                            getUserDetailsUiModel("user3"),
                            getUserDetailsUiModel("user4"),
                            getUserDetailsUiModel("user5")
                        ),
                        isLoadMore = false
                    )
                )
            )
        )

        viewModel.getUserList()
        advanceUntilIdle()

        assertFalse(viewModel.canPaginate)
    }

    @Test
    fun `test getUserList null data`() = runTest {
        every {
            userUseCase.getUserList(any(), any())
        }.returns(
            flowOf(
                Resource.Success(null)
            )
        )
        val viewModel = newViewModel()
        viewModel.getUserList()

        advanceUntilIdle()

        assertEquals(0, viewModel.userList.value.size)
        assertFalse(viewModel.canPaginate)
    }

    @Test
    fun `test getUserList error`() = runTest {
        every {
            userUseCase.getUserList(any(), any())
        }.returns(
            flowOf(Resource.Error(ResourceError(ResourceError.ErrorType.GENERIC, "error message")))
        )
        val viewModel = newViewModel()
        viewModel.getUserList()

        advanceUntilIdle()

        assertEquals("error message", viewModel.generalError.value)
        assertEquals(0, viewModel.userList.value.size)
    }

    @Test
    fun `test getUserDetails no network`() = runTest {
        every {
            userUseCase.getUserList(any(), any())
        }.returns(
            flowOf(
                Resource.Error(
                    ResourceError(
                        ResourceError.ErrorType.NETWORK_UNAVAILABLE,
                        "error message"
                    )
                )
            )
        )
        val viewModel = newViewModel()
        viewModel.getUserList()

        advanceUntilIdle()

        TestCase.assertTrue(viewModel.networkUnavailableEvent.value)
    }

    private fun getUserDetailsUiModel(username: String = "") =
        UserDetailsUiModel(username)

}