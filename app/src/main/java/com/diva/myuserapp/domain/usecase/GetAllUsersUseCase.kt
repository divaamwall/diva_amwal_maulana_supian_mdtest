package com.diva.myuserapp.domain.usecase

import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.model.User
import com.diva.myuserapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<Resource<List<User>>> {
        return userRepository.getAllUsers()
    }
}