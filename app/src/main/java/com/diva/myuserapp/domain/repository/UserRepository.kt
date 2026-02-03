package com.diva.myuserapp.domain.repository

import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun saveUser(user: User): Resource<Unit>

    fun getAllUsers(): Flow<Resource<List<User>>>

    suspend fun getUserById(uid: String): Resource<User>

    suspend fun updateUser(user: User): Resource<Unit>
}