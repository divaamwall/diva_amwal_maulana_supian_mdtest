package com.diva.myuserapp.domain.usecase

import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.model.User
import com.diva.myuserapp.domain.repository.AuthRepository
import javax.inject.Inject

class GetCurrentUserUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): User? {
        return when (val result = authRepository.reloadUser()) {
            is Resource.Success -> result.data
            else -> authRepository.getCurrentUser()
        }
    }
}