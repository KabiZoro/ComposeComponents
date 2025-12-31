package com.kabi.composecomponents.google.credentials.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kabi.composecomponents.google.credentials.SignInResult
import com.kabi.composecomponents.google.credentials.SignUpResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(LoginState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = LoginState()
        )

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnPasswordChange -> {
                _state.update {
                    it.copy(password = action.password)
                }
            }

            is LoginAction.OnSignUp -> {
                when (action.result) {
                    SignUpResult.Cancelled -> {
                        _state.update {
                            it.copy(errorMessage = "SignUp cancelled.")
                        }
                    }

                    SignUpResult.Failure -> {
                        _state.update {
                            it.copy(errorMessage = "SignUp failed.")
                        }
                    }

                    is SignUpResult.Success -> {
                        _state.update {
                            it.copy(loggedInUser = action.result.username)
                        }
                    }
                }
            }

            is LoginAction.OnSignIn -> {
                when (action.result) {
                    SignInResult.Cancelled -> {
                        _state.update {
                            it.copy(errorMessage = "SignIn cancelled.")
                        }
                    }

                    SignInResult.Failure -> {
                        _state.update {
                            it.copy(errorMessage = "SignIn failed.")
                        }
                    }

                    is SignInResult.Success -> {
                        _state.update {
                            it.copy(loggedInUser = action.result.username)
                        }
                    }

                    SignInResult.NoCredentials -> {
                        _state.update {
                            it.copy(errorMessage = "No credentials were setup.")
                        }
                    }
                }
            }

            LoginAction.OnToggleIsRegister -> {
                _state.update {
                    it.copy(isRegister = !it.isRegister)
                }
            }

            is LoginAction.OnUsernameChange -> {
                _state.update {
                    it.copy(username = action.username)
                }
            }

        }
    }

}