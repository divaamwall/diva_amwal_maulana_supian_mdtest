package com.diva.myuserapp.domain.usecase

import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.repository.AuthRepository
import com.diva.myuserapp.utils.EmailValidator
import javax.inject.Inject

class SendPasswordResetUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String): Resource<Unit> {
        // Validate input
        if (email.isBlank()) {
            return Resource.Error("Email tidak boleh kosong")
        }

        if (!EmailValidator.isValid(email)) {
            return Resource.Error("Format email salah")
        }

        return authRepository.sendPasswordResetEmail(email)
    }
}