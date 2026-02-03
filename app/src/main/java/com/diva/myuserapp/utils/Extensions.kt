package com.diva.myuserapp.utils

import com.diva.myuserapp.domain.model.User
import com.google.firebase.auth.FirebaseUser

fun FirebaseUser.toDomainUser(): User {
    return User(
        uid = uid,
        name = displayName ?: "",
        email = email ?: "",
        emailVerified = isEmailVerified
    )
}