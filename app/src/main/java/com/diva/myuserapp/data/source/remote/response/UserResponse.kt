package com.diva.myuserapp.data.source.remote.response

import com.diva.myuserapp.domain.model.User
import com.google.firebase.firestore.PropertyName

data class UserResponse(
    @PropertyName("uid")
    val uid: String = "",

    @PropertyName("name")
    val name: String = "",

    @PropertyName("email")
    val email: String = "",

    @PropertyName("emailVerified")
    val emailVerified: Boolean = false,

    @PropertyName("createdAt")
    val createdAt: Long = System.currentTimeMillis()
) {
    fun toDomain(): User {
        return User(
            uid = uid,
            name = name,
            email = email,
            emailVerified = emailVerified,
            createdAt = createdAt
        )
    }

    companion object {
        fun fromDomain(user: User): UserResponse {
            return UserResponse(
                uid = user.uid,
                name = user.name,
                email = user.email,
                emailVerified = user.emailVerified,
                createdAt = user.createdAt
            )
        }
    }
}
