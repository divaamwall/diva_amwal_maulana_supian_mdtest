package com.diva.myuserapp.domain.usecase

import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.model.User
import com.diva.myuserapp.domain.repository.AuthRepository
import com.diva.myuserapp.domain.repository.UserRepository
import javax.inject.Inject

class ReloadUserUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Resource<User> {
        return when (val result = authRepository.reloadUser()){
            is Resource.Success -> {
                result.data?.let { user ->
                    userRepository.updateUser(user)

                }
                result
            }
            is Resource.Error -> result
            else -> Resource.Error("Terjadi permasalahan sistem")
        }
    }
}