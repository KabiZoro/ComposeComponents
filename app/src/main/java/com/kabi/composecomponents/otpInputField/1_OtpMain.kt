package com.kabi.composecomponents.otpInputField

import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kabi.composecomponents.ui.theme.CodingGrey

@Composable
fun OtpMain() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = CodingGrey
    ) { paddingValues ->
        val viewModel = viewModel<OtpViewModel>()
        val state by viewModel.state.collectAsStateWithLifecycle()
        val focusRequesters = remember {
//            (1..4).map { FocusRequester() }
            List(4) { FocusRequester() }
        }
        val focusManager = LocalFocusManager.current
        val keyboardManager = LocalSoftwareKeyboardController.current

        LaunchedEffect(state.focusedIndex) {
            state.focusedIndex?.let { index ->
                focusRequesters.getOrNull(index)?.requestFocus()
            }
        }

        LaunchedEffect(state.code, keyboardManager) {
            val allNumbersEntered = state.code.none { it == null }
            if (allNumbersEntered) {
                focusRequesters.forEach { it.freeFocus() }
                focusManager.clearFocus()
                keyboardManager?.hide()
            }
        }

        OtpScreen(
            state = state,
            focusRequesters = focusRequesters,
            onAction = { action ->
                when (action) {
                    is OtpAction.OnEnterNumber -> {
                        if (action.number != null) {
                            focusRequesters[action.index].freeFocus()
                        }
                    }

                    else -> Unit
                }
                viewModel.onAction(action)
            },
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
        )
    }
}

@Preview
@Composable
private fun OtpMainPreview() {
    OtpMain()
}