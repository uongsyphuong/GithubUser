package com.example.githubuser.viewmodel

import com.example.githubuser.model.resource.Resource
import com.example.githubuser.model.resource.ResourceError
import com.example.githubuser.ui.uistate.UserDetailsUiModel
import com.example.githubuser.usecase.UserUseCase
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Test

@ExperimentalCoroutinesApi
class UserDetailsViewModelTest : BaseViewModelTestCase<UserDetailsViewModel>() {

    @MockK
    private lateinit var userUseCase: UserUseCase

    override fun newViewModel(): UserDetailsViewModel {
        return UserDetailsViewModel(userUseCase)
    }


    @Test
    fun `test getUserDetails have data`() = runTest {
        every {
            userUseCase.getUserDetails(any())
        }.returns(
            flowOf(
                Resource.Success(
                    getUserDetailsUiModel()
                )
            )
        )
        val viewModel = newViewModel()
        viewModel.getUserDetails("user1")
        advanceUntilIdle()

        assertEquals("user1", viewModel.userDetails.value.username)
    }

    @Test
    fun `test getUserDetails empty`() = runTest {
        every {
            userUseCase.getUserDetails(any())
        }.returns(
            flowOf(
                Resource.Success(null)
            )
        )
        val viewModel = newViewModel()
        viewModel.getUserDetails("user1")

        advanceUntilIdle()

        assertEquals("", viewModel.userDetails.value.username)
    }

    @Test
    fun `test getUserDetails error`() = runTest {
        every {
            userUseCase.getUserDetails(any())
        }.returns(
            flowOf(Resource.Error(ResourceError(ResourceError.ErrorType.GENERIC, "error message")))
        )
        val viewModel = newViewModel()
        viewModel.getUserDetails("user1")

        advanceUntilIdle()

        assertEquals("error message", viewModel.generalError.value)
    }

    @Test
    fun `test getUserDetails no network`() = runTest {
        every {
            userUseCase.getUserDetails(any())
        }.returns(
            flowOf(Resource.Error(ResourceError(ResourceError.ErrorType.NETWORK_UNAVAILABLE, "error message")))
        )
        val viewModel = newViewModel()
        viewModel.getUserDetails("user1")

        advanceUntilIdle()

        assertTrue(viewModel.networkUnavailableEvent.value)
    }

    private fun getUserDetailsUiModel() = UserDetailsUiModel("user1")
}