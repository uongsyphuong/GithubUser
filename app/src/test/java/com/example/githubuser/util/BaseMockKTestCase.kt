package com.example.githubuser.util

import androidx.annotation.CallSuper
import io.mockk.MockKAnnotations
import okio.buffer
import okio.source
import org.junit.Before
import java.io.IOException
import java.nio.charset.StandardCharsets

abstract class BaseMockKTestCase {

    @Before
    @CallSuper
    open fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
    }

    /**
     * Returns the content of a file in the `resources/api-response` directory as a String.
     *
     * @param name The file name
     * @return The file content
     */
    protected fun getResourceAsString(name: String): String {
        val inputStream = javaClass.classLoader?.getResourceAsStream(name)
        return try {
            val source = inputStream?.source()?.buffer()
            source?.readString(StandardCharsets.UTF_8).orEmpty()
        } catch (e: IOException) {
            throw RuntimeException("Error reading $name\n$e")
        }
    }
}