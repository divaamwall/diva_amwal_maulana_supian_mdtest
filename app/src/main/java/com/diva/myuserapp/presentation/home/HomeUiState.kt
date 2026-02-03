package com.diva.myuserapp.presentation.home

import com.diva.myuserapp.domain.model.User

data class HomeUiState(
    val currentUser: User? = null,
    val allUsers: List<User> = emptyList(),
    val filteredUsers: List<User> = emptyList(),
    val searchQuery: String = "",
    val verificationFilter: VerificationFilter = VerificationFilter.ALL,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isRefreshing: Boolean = false
)

enum class VerificationFilter {
    ALL,
    VERIFIED,
    NOT_VERIFIED
}

