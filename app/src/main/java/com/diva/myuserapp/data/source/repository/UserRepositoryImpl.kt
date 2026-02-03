package com.diva.myuserapp.data.source.repository

import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.data.source.remote.FirestoreDataSource
import com.diva.myuserapp.data.source.remote.response.UserResponse
import com.diva.myuserapp.domain.model.User
import com.diva.myuserapp.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firestoreDataSource: FirestoreDataSource
) : UserRepository {
    override suspend fun saveUser(user: User): Resource<Unit> {
        return try {
            val userResponse = UserResponse.fromDomain(user)
            firestoreDataSource.saveUser(userResponse)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Gagal menyimpan pengguna")
        }
    }

    override fun getAllUsers(): Flow<Resource<List<User>>> {
        return firestoreDataSource.getAllUsers()
            .map { userResponses ->
                val users = userResponses.map { it.toDomain() }
                Resource.Success(users) as Resource<List<User>>
            }
            .catch { e ->
                emit(Resource.Error(e.message ?: "Gagal menampilkan data seluruh pengguna"))
            }
    }

    override suspend fun getUserById(uid: String): Resource<User> {
        return try {
            val userResponse = firestoreDataSource.getUserById(uid)
            if (userResponse != null) {
                Resource.Success(userResponse.toDomain())
            } else {
                Resource.Error("Pengguna tidak diketahui")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Gagal menampilkan data pengguna")
        }
    }

    override suspend fun updateUser(user: User): Resource<Unit> {
        return try {
            val userResponse = UserResponse.fromDomain(user)
            firestoreDataSource.updateUser(userResponse)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Gagal mengubah data pengguna")
        }
    }
}