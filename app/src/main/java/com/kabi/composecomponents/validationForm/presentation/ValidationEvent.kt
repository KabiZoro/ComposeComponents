package com.kabi.composecomponents.validationForm.presentation

sealed class ValidationEvent{
    object Success: ValidationEvent()
}