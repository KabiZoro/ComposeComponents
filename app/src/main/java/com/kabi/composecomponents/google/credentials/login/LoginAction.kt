package com.kabi.composecomponents.google.credentials.login

import com.kabi.composecomponents.google.credentials.SignInResult
import com.kabi.composecomponents.google.credentials.SignUpResult

sealed interface LoginAction {
    data class OnSignUp(val result: SignUpResult): LoginAction
    data class OnSignIn(val result: SignInResult): LoginAction
    data class OnUsernameChange(val username: String): LoginAction
    data class OnPasswordChange(val password: String): LoginAction
    data object OnToggleIsRegister: LoginAction
}