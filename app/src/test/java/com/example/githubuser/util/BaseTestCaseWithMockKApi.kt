package com.example.githubuser.util

import com.example.githubuser.api.ResourceFlowCallAdapterFactory
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

open class BaseTestCaseWithMockKApi : BaseMockKTestCase() {

    protected var server: MockWebServer? = null

    @After
    @Throws(Exception::class)
    fun tearDown() {
        if (server != null) {
            server!!.shutdown()
            server = null
        }
    }

    protected fun startMockServer(vararg responses: MockResponse) {
        server = MockWebServer()
        for (response in responses) {
            server!!.enqueue(response)
        }
        try {
            server!!.start()
        } catch (e: Exception) {
            println("Unable to start mock server: " + e.message)
        }
    }

    fun getRetrofit(ioException: IOException?): Retrofit {
        if (server == null) {
            startMockServer()
        }
        val mockServerUrl = server?.url("/")
            ?: throw RuntimeException("Mock server is not started")

        val okHttpBuilder = OkHttpClient.Builder()

        if (ioException != null) {
            okHttpBuilder.addInterceptor(Interceptor { throw ioException })
        } else {
            okHttpBuilder.addInterceptor(Interceptor { chain -> // Redirect all requests to internal mock server
                var original = chain.request()
                val newUrl = original.url
                    .newBuilder()
                    .scheme(mockServerUrl.scheme)
                    .host(mockServerUrl.host)
                    .port(mockServerUrl.port)
                    .build()
                original = original.newBuilder().url(newUrl).build()
                chain.proceed(original)
            })
        }

        val gsonBuilder = GsonBuilder()
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)

        val retrofit = Retrofit.Builder()
            .client(okHttpBuilder.build())
            .baseUrl(mockServerUrl)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder.create()))
            .addCallAdapterFactory(ResourceFlowCallAdapterFactory())
            .build()

        return retrofit
    }

    /**
     * Returns a new [MockResponse] object with the body set to the content of a file in the
     * `resources/api-response` directory.
     *
     * @param name The file name
     * @return A [MockResponse] object
     */
    protected fun mockResponseFromFile(name: String): MockResponse {
        return MockResponse().setBody(getResourceAsString("api-response/$name"))
    }

}
