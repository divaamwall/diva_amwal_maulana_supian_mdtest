package com.diva.myuserapp.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.usecase.SignUpUseCase
import com.diva.myuserapp.utils.EmailValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNameChange(name: String) {
        _uiState.update {
            it.copy(
                name = name,
                error = null,
                nameValidations = validateName(name)
            )
        }
        updateFormValidity()
    }

    fun onEmailChange(email: String) {
        _uiState.update {
            it.copy(
                email = email,
                error = null,
                emailValidations = validateEmail(email)
            )
        }
        updateFormValidity()
    }

    fun onPasswordChange(password: String) {
        _uiState.update {
            it.copy(
                password = password,
                error = null,
                passwordValidations = validatePassword(password)
            )
        }
        // Re-validate confirm password when password changes
        if (_uiState.value.confirmPassword.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    confirmPasswordValidations = validateConfirmPassword(
                        password,
                        _uiState.value.confirmPassword
                    )
                )
            }
        }
        updateFormValidity()
    }

    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update {
            it.copy(
                confirmPassword = confirmPassword,
                error = null,
                confirmPasswordValidations = validateConfirmPassword(
                    _uiState.value.password,
                    confirmPassword
                )
            )
        }
        updateFormValidity()
    }

    private fun validateName(name: String): List<FieldValidation> {
        return listOf(
            FieldValidation(
                message = "Nama Minimal 3 karakter",
                state = when {
                    name.isEmpty() -> ValidationState.NONE
                    name.length >= 3 -> ValidationState.VALID
                    else -> ValidationState.INVALID
                }
            ),
            FieldValidation(
                message = "Nama tidak mengandung angka",
                state = when {
                    name.isEmpty() -> ValidationState.NONE
                    !name.any { it.isDigit() } -> ValidationState.VALID
                    else -> ValidationState.INVALID
                }
            )
        )
    }

    private fun validateEmail(email: String): List<FieldValidation> {
        val emailPattern = android.util.Patterns.EMAIL_ADDRESS

        return listOf(
            FieldValidation(
                message = "Email tidak boleh kosong",
                state = when {
                    email.isEmpty() -> ValidationState.NONE
                    email.isNotEmpty() -> ValidationState.VALID
                    else -> ValidationState.INVALID
                }
            ),
            FieldValidation(
                message = "Email harus menggunakan format yang valid",
                state = when {
                    email.isEmpty() -> ValidationState.NONE
                    EmailValidator.isValid(email) -> ValidationState.VALID
                    else -> ValidationState.INVALID
                }
            )
        )
    }

    private fun validatePassword(password: String): List<FieldValidation> {
        return listOf(
            FieldValidation(
                message = "Password minimal 6 karakter",
                state = when {
                    password.isEmpty() -> ValidationState.NONE
                    password.length >= 6 -> ValidationState.VALID
                    else -> ValidationState.INVALID
                }
            ),
            FieldValidation(
                message = "Password mengandung huruf kecil",
                state = when {
                    password.isEmpty() -> ValidationState.NONE
                    password.any { it.isLowerCase() } -> ValidationState.VALID
                    else -> ValidationState.INVALID
                }
            ),
            FieldValidation(
                message = "Password mengandung huruf besar",
                state = when {
                    password.isEmpty() -> ValidationState.NONE
                    password.any { it.isUpperCase() } -> ValidationState.VALID
                    else -> ValidationState.INVALID
                }
            ),
            FieldValidation(
                message = "Password mengandung angka",
                state = when {
                    password.isEmpty() -> ValidationState.NONE
                    password.any { it.isDigit() } -> ValidationState.VALID
                    else -> ValidationState.INVALID
                }
            ),
            FieldValidation(
                message = "Password tidak mengandung spasi",
                state = when {
                    password.isEmpty() -> ValidationState.NONE
                    !password.contains(" ") -> ValidationState.VALID
                    else -> ValidationState.INVALID
                }
            )
        )
    }

    private fun validateConfirmPassword(password: String, confirmPassword: String): List<FieldValidation> {
        return listOf(
            FieldValidation(
                message = "Konfirmasi Password sama dengan Password",
                state = when {
                    confirmPassword.isEmpty() -> ValidationState.NONE
                    password == confirmPassword -> ValidationState.VALID
                    else -> ValidationState.INVALID
                }
            )
        )
    }

    private fun updateFormValidity() {
        val currentState = _uiState.value

        val isNameValid = currentState.nameValidations.all {
            it.state == ValidationState.VALID
        } && currentState.nameValidations.isNotEmpty()

        val isEmailValid = currentState.emailValidations.all {
            it.state == ValidationState.VALID
        } && currentState.emailValidations.isNotEmpty()

        val isPasswordValid = currentState.passwordValidations.all {
            it.state == ValidationState.VALID
        } && currentState.passwordValidations.isNotEmpty()

        val isConfirmPasswordValid = currentState.confirmPasswordValidations.all {
            it.state == ValidationState.VALID
        } && currentState.confirmPasswordValidations.isNotEmpty()

        _uiState.update {
            it.copy(
                isFormValid = isNameValid &&
                        isEmailValid &&
                        isPasswordValid &&
                        isConfirmPasswordValid
            )
        }
    }

    fun signUp() {
        if (!_uiState.value.isFormValid) {
            _uiState.update {
                it.copy(error = "Silakan isi kolom dengan benar")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            when (val result = signUpUseCase(
                name = _uiState.value.name,
                email = _uiState.value.email,
                password = _uiState.value.password
            )) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            error = null
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
                else -> {
                    _uiState.update { it.copy(isLoading = false) }
                }
            }
        }
    }


    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}