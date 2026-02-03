package com.diva.myuserapp.domain.repository

import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun signIn(email: String, password: String): Resource<User>

    suspend fun signUp(name: String, email: String, password: String): Resource<User>

    suspend fun sendPasswordResetEmail(email: String): Resource<Unit>

    suspend fun sendEmailVerification(): Resource<Unit>

    suspend fun signOut()

    suspend fun getCurrentUser(): User?

    suspend fun reloadUser(): Resource<User>

    fun observeAuthState(): Flow<User?>
}