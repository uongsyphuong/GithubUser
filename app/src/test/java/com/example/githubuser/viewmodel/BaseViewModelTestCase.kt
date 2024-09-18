package com.example.githubuser.viewmodel

import androidx.annotation.CallSuper
import com.example.githubuser.util.BaseMockKTestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before

@ExperimentalCoroutinesApi
abstract class BaseViewModelTestCase<T : BaseViewModel> : BaseMockKTestCase() {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    @CallSuper
    override fun setUp() {
        super.setUp()
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /**
     * Returns a new instance of the view model that is being tested.
     *
     * @return the view model that is being tested
     */
    internal abstract fun newViewModel(): T

}
