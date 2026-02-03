package com.diva.myuserapp.domain.usecase

import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.model.User
import com.diva.myuserapp.domain.repository.AuthRepository
import com.diva.myuserapp.domain.repository.UserRepository
import com.diva.myuserapp.utils.EmailValidator
import javax.inject.Inject

class SignInUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): Resource<User> {
        // Validation
        if (email.isBlank() || password.isBlank()) {
            return Resource.Error("Email dan password tidak boleh kosong")
        }

        if (!EmailValidator.isValid(email)) {
            return Resource.Error("Format email salah")
        }

        if (password.length < 6) {
            return Resource.Error("Password seharusnya 6 karakter atau lebih")
        }

        // Login
        return when (val result = authRepository.signIn(email, password)) {
            is Resource.Success -> {
                result.data?.let { user ->
                    userRepository.updateUser(user)
                }
                result
            }
            is Resource.Error -> result
            else -> result
        }
    }
}