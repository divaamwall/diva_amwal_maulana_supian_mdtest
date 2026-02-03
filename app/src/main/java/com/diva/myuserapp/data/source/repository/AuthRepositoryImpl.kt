package com.diva.myuserapp.data.source.repository

import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.data.source.remote.FirebaseAuthDataSource
import com.diva.myuserapp.domain.model.User
import com.diva.myuserapp.domain.repository.AuthRepository
import com.diva.myuserapp.utils.toDomainUser
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authDataSource: FirebaseAuthDataSource
) : AuthRepository {

    override suspend fun signIn(email: String, password: String): Resource<User> {
        return try {
            val firebaseUser = authDataSource.signIn(email, password)
            if (firebaseUser != null) {
                Resource.Success(firebaseUser.toDomainUser())
            } else {
                Resource.Error("Login gagal")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi permasalahan pada saat login")
        }
    }

    override suspend fun signUp(name: String, email: String, password: String): Resource<User> {
        return try {
            val firebaseUser = authDataSource.signUp(name, email, password)
            if (firebaseUser != null) {
                Resource.Success(firebaseUser.toDomainUser())
            } else {
                Resource.Error("Register gagal")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi permasalahan pada saat register")
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Resource<Unit> {
        return try {
            authDataSource.sendPasswordResetEmail(email)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Gagal mengirim reset password ke email")
        }
    }

    override suspend fun sendEmailVerification(): Resource<Unit> {
        return try {
            authDataSource.sendEmailVerification()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Gagal mengirimkan verifikasi email")
        }
    }

    override suspend fun signOut() {
        authDataSource.signOut()
    }

    override suspend fun getCurrentUser(): User? {
        return authDataSource.getCurrentUser()?.toDomainUser()
    }

    override suspend fun reloadUser(): Resource<User> {
        return try {
            val firebaseUser = authDataSource.reloadUser()
            if (firebaseUser != null) {
                Resource.Success(firebaseUser.toDomainUser())
            } else {
                Resource.Error("Gagal menampilkan data pengguna")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Terjadi permasalahan saat menampilkan data pengguna")
        }
    }

    override fun observeAuthState(): Flow<User?> {
        return authDataSource.observeAuthState().map { firebaseUser ->
            firebaseUser?.toDomainUser()
        }
    }
}