package com.example.githubuser.repository

import com.example.githubuser.api.ApiService
import com.example.githubuser.model.resource.Resource
import com.example.githubuser.util.BaseTestCaseWithMockKApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.ExperimentalTime

class UserRepositoryTest : BaseTestCaseWithMockKApi() {

    private fun newRepository(apiService: ApiService) = UserRepositoryImpl(apiService)

    @ExperimentalTime
    @Test
    fun `getUserList on success response`() {
        startMockServer(mockResponseFromFile("user_list_first.json"))
        val retrofit = getRetrofit(null)
        val apiService = retrofit.create(ApiService::class.java)

        val repository = newRepository(apiService)

        val resource = runBlocking { repository.getUserList("20", "0").first() }

        assertTrue(resource is Resource.Success)
        assertNotNull((resource as Resource.Success).data)
    }

    @ExperimentalTime
    @Test
    fun `getUserList on error response`() {
        startMockServer(MockResponse().setResponseCode(400))
        val retrofit = getRetrofit(null)
        val apiService = retrofit.create(ApiService::class.java)

        val repository = newRepository(apiService)

        val resource = runBlocking { repository.getUserList("20", "0").first() }

        assertTrue(resource is Resource.Error)
    }

    @ExperimentalTime
    @Test
    fun `getUserDetails on success response`() {
        startMockServer(mockResponseFromFile("user_details.json"))
        val retrofit = getRetrofit(null)
        val apiService = retrofit.create(ApiService::class.java)

        val repository = newRepository(apiService)

        val resource = runBlocking { repository.getUserDetails("username").first() }

        assertTrue(resource is Resource.Success)
        assertNotNull((resource as Resource.Success).data)
    }

    @ExperimentalTime
    @Test
    fun `getUserDetails on error response`() {
        startMockServer(MockResponse().setResponseCode(400))
        val retrofit = getRetrofit(null)
        val apiService = retrofit.create(ApiService::class.java)

        val repository = newRepository(apiService)

        val resource = runBlocking { repository.getUserDetails("username").first() }

        assertTrue(resource is Resource.Error)
    }

}