package com.diva.myuserapp.domain.model

data class User(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val emailVerified: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
