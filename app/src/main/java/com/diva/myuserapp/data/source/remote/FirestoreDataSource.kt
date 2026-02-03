package com.diva.myuserapp.data.source.remote

import com.diva.myuserapp.data.source.remote.response.UserResponse
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    companion object {
        private const val USERS_COLLECTION = "users"
    }

    suspend fun saveUser(user: UserResponse) {
        firestore.collection(USERS_COLLECTION)
            .document(user.uid)
            .set(user)
            .await()
    }

    fun getAllUsers(): Flow<List<UserResponse>> = callbackFlow {
        val subscription = firestore.collection(USERS_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val users = snapshot?.documents?.mapNotNull { doc ->
                    try {
                        doc.toObject(UserResponse::class.java)
                    } catch (e: Exception) {
                        null
                    }
                } ?: emptyList()

                trySend(users)
            }

        awaitClose { subscription.remove() }
    }

    suspend fun getUserById(uid: String): UserResponse? {
        val document = firestore.collection(USERS_COLLECTION)
            .document(uid)
            .get()
            .await()

        return document.toObject(UserResponse::class.java)
    }

    suspend fun updateUser(user: UserResponse) {
        firestore.collection(USERS_COLLECTION)
            .document(user.uid)
            .set(user)
            .await()
    }
}