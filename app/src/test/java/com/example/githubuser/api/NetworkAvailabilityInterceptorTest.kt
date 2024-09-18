package com.example.githubuser.api

import com.example.githubuser.util.BaseMockKTestCase
import com.example.githubuser.utils.NetworkHelper
import io.mockk.every
import io.mockk.impl.annotations.MockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Test
import java.io.IOException

class NetworkAvailabilityInterceptorTest : BaseMockKTestCase() {
    @MockK
    private lateinit var mNetworkHelper: NetworkHelper

    @Test
    @Throws(IOException::class)
    fun intercept_networkAvailable() {
        val mockWebServer = MockWebServer()

        every { mNetworkHelper.isConnected() } returns true
        mockWebServer.enqueue(MockResponse().setBody("OK"))
        mockWebServer.start()

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(NetworkAvailabilityInterceptor(mNetworkHelper))
            .build()
        val request = Request.Builder()
            .url(mockWebServer.url("/"))
            .build()

        val response = okHttpClient.newCall(request).execute()
        val responseBody = response.body

        assertNotNull(responseBody)
        assertEquals("OK", responseBody!!.string())
    }

    @Test(expected = NetworkUnavailableException::class)
    @Throws(Exception::class)
    fun intercept_networkUnavailable() {
        every { mNetworkHelper.isConnected() } returns false
        val server = MockWebServer()
        server.enqueue(MockResponse().setBody("OK"))
        server.start()
        val okHttpBuilder = OkHttpClient.Builder()
        okHttpBuilder.addInterceptor(NetworkAvailabilityInterceptor(mNetworkHelper))
        val request = Request.Builder().url(server.url("/")).build()
        okHttpBuilder.build().newCall(request).execute()
    }
}
