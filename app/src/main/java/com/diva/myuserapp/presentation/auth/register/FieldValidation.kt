package com.diva.myuserapp.presentation.auth.register

data class FieldValidation(
    val message: String,
    val state: ValidationState = ValidationState.NONE
)
