package com.example.githubuser.usecase

import com.example.githubuser.model.api.UserResponse
import com.example.githubuser.model.resource.Resource
import com.example.githubuser.model.resource.ResourceError
import com.example.githubuser.repository.UserRepository
import com.example.githubuser.storage.AppDataStorage
import com.example.githubuser.util.BaseMockKTestCase
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class UserUseCaseTest : BaseMockKTestCase() {

    @MockK
    private lateinit var mUserRepository: UserRepository

    @MockK
    private lateinit var appDataStorage: AppDataStorage

    private fun newUseCase() = UserUseCaseImpl(mUserRepository, appDataStorage)

    @Before
    override fun setUp() {
        super.setUp()
        coEvery { appDataStorage.getUserListFromInternalStorage() } returns (null)
    }

    @Test
    fun `getUsers when have local data`() = runBlocking {
        val userResponseList = listOf(
            getUserResponse("testuser1"),
            getUserResponse("testuser2"),
            getUserResponse("testuser3"),
            getUserResponse("testuser4"),
            getUserResponse("testuser5")
        )
        coEvery { appDataStorage.getUserListFromInternalStorage() } returns (
                userResponseList
                )

        val userUseCase = newUseCase()
        userUseCase.getUserList(5, 0).collect {
            Assert.assertTrue(it is Resource.Success)
            Assert.assertNotNull((it as Resource.Success).data)
            Assert.assertTrue(it.data?.isLoadMore!!)
        }
    }

    @Test
    fun `getUsers returns success`() = runBlocking {
        val userResponseList = listOf(
            getUserResponse("testuser1"),
            getUserResponse("testuser2"),
            getUserResponse("testuser3"),
            getUserResponse("testuser4"),
            getUserResponse("testuser5")
        )
        val userResponseList2 = listOf(
            getUserResponse("testuser6"),
            getUserResponse("testuser7"),
            getUserResponse("testuser8"),
            getUserResponse("testuser9")
        )

        coEvery { mUserRepository.getUserList("5", "0") } returns flowOf(
            Resource.Success(userResponseList)
        )
        coEvery { mUserRepository.getUserList("5", "5") } returns flowOf(
            Resource.Success(userResponseList2)
        )

        val userUseCase = newUseCase()
        userUseCase.getUserList(5, 0).collect {
            Assert.assertTrue(it is Resource.Success)
            Assert.assertNotNull((it as Resource.Success).data)
            Assert.assertTrue(it.data?.isLoadMore!!)

            coEvery { appDataStorage.getUserListFromInternalStorage() } returns (userResponseList)

            userUseCase.getUserList(5, 5).collect {
                Assert.assertTrue(it is Resource.Success)
                Assert.assertNotNull((it as Resource.Success).data)
                Assert.assertFalse(it.data?.isLoadMore!!)
            }
        }
    }

    @Test
    fun `getUsers returns success null data`() = runBlocking {
        coEvery { mUserRepository.getUserList("5", "0") } returns flowOf(
            Resource.Success(null)
        )

        val userUseCase = newUseCase()
        userUseCase.getUserList(5, 0).collect {
            Assert.assertTrue(it is Resource.Success)
            Assert.assertNotNull((it as Resource.Success).data)
            Assert.assertEquals(0, it.data?.userListItem?.size)
            Assert.assertFalse(it.data?.isLoadMore!!)
        }
    }


    @Test
    fun `getUsers returns success empty data`() = runBlocking {
        coEvery { mUserRepository.getUserList("5", "0") } returns flowOf(
            Resource.Success(emptyList())
        )

        val userUseCase = newUseCase()
        userUseCase.getUserList(5, 0).collect {
            Assert.assertTrue(it is Resource.Success)
            Assert.assertNotNull((it as Resource.Success).data)
            Assert.assertEquals(0, it.data?.userListItem?.size)
            Assert.assertFalse(it.data?.isLoadMore!!)
        }
    }

    @Test
    fun `getUsers returns error`() = runBlocking {
        coEvery { mUserRepository.getUserList(any(), any()) } returns flowOf(
            Resource.Error(ResourceError())
        )
        val userUseCase = newUseCase()
        userUseCase.getUserList(5, 0).collect {
            Assert.assertTrue(it is Resource.Error)
        }
    }

    @Test
    fun `getUserDetails returns success`() = runBlocking {
        coEvery { mUserRepository.getUserDetails(any()) } returns flowOf(
            Resource.Success(getUserResponse())
        )
        val userUseCase = newUseCase()
        userUseCase.getUserDetails("testuser").collect {
            Assert.assertTrue(it is Resource.Success)
            Assert.assertNotNull((it as Resource.Success).data)
            Assert.assertEquals("testuser", it.data?.username)
            Assert.assertEquals(10, it.data?.followers)
        }
    }

    @Test
    fun `getUserDetails returns success with empty data`() = runBlocking {
        coEvery { mUserRepository.getUserDetails(any()) } returns flowOf(
            Resource.Success(null)
        )
        val userUseCase = newUseCase()
        userUseCase.getUserDetails("testuser").collect {
            Assert.assertTrue(it is Resource.Success)
            Assert.assertNull((it as Resource.Success).data)
        }
    }

    @Test
    fun `getUserDetails returns success with null field`() = runBlocking {
        coEvery { mUserRepository.getUserDetails(any()) } returns flowOf(
            Resource.Success(getUserResponse(null, null, null, null, null, null))
        )
        val userUseCase = newUseCase()
        userUseCase.getUserDetails("testuser").collect {
            Assert.assertTrue(it is Resource.Success)
            Assert.assertNotNull((it as Resource.Success).data)
            Assert.assertEquals("", it.data?.username)
            Assert.assertEquals(0, it.data?.followers)
        }
    }

    @Test
    fun `getUserDetails returns error`() = runBlocking {
        coEvery { mUserRepository.getUserDetails(any()) } returns flowOf(
            Resource.Error(ResourceError())
        )
        val userUseCase = newUseCase()
        userUseCase.getUserDetails("testuser").collect {
            Assert.assertTrue(it is Resource.Error)
        }
    }

    private fun getUserResponse(
        username: String? = "testuser",
        avatarUrl: String? = "avatarUrl",
        htmlUrl: String? = "htmlUrl",
        location: String? = "location",
        followers: Int? = 10,
        following: Int? = 20
    ) =
        UserResponse(username, avatarUrl, htmlUrl, location, followers, following)
}