package com.example.githubuser.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.lifecycleScope
import com.example.githubuser.viewmodel.BaseViewModel
import kotlinx.coroutines.launch

abstract class BaseActivity : ComponentActivity() {

    protected abstract val viewModel: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            viewModel.generalError.collect { errorMessage ->
                if (errorMessage != null) {
                    showError(errorMessage)
                }
            }
        }

        lifecycleScope.launch {
            viewModel.networkUnavailableEvent.collect { isNetworkUnavailable ->
                if (isNetworkUnavailable) {
                    showNetworkUnavailableError()
                }
            }
        }
        setContent {
            ComposeContent()
        }
    }

    private fun showError(errorMessage: String) {
        // Implement error handling logic
    }

    private fun showNetworkUnavailableError() {
        // Implement network unavailable error handling logic
    }


    @Composable
    abstract fun ComposeContent()
}