package com.diva.myuserapp.presentation.auth.register

data class RegisterUiState(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSuccess: Boolean = false,

    //Validation
    val nameValidations: List<FieldValidation> = emptyList(),
    val emailValidations: List<FieldValidation> = emptyList(),
    val passwordValidations: List<FieldValidation> = emptyList(),
    val confirmPasswordValidations: List<FieldValidation> = emptyList(),

    //Form Validation
    val isFormValid: Boolean = false
)
