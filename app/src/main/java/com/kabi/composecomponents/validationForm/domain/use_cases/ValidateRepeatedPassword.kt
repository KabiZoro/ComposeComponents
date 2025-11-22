package com.kabi.composecomponents.validationForm.domain.use_cases

class ValidateRepeatedPassword {

    fun execute(password: String, repeatedPassword: String): ValidationResult {
        if (password != repeatedPassword) {
            return ValidationResult(
                successful = false,
                errorMessage = "The passwords don't match"
            )
        }
        return ValidationResult(
            successful = true
        )
    }

}