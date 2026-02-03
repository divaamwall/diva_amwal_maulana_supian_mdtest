package com.diva.myuserapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.usecase.GetAllUsersUseCase
import com.diva.myuserapp.domain.usecase.GetCurrentUserUseCase
import com.diva.myuserapp.domain.usecase.ReloadUserUseCase
import com.diva.myuserapp.domain.usecase.SignOutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getAllUsersUseCase: GetAllUsersUseCase,
    private val reloadUserUseCase: ReloadUserUseCase,
    private val signOutUseCase: SignOutUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadCurrentUser()
        syncUserDataOnInit()
        loadAllUsers()
    }

    private fun syncUserDataOnInit() {
        viewModelScope.launch {
            reloadUserUseCase()
        }
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            val user = getCurrentUserUseCase()
            _uiState.update { it.copy(currentUser = user) }
        }
    }

    private fun loadAllUsers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            getAllUsersUseCase()
                .collect { result ->
                    when (result) {
                        is Resource.Success -> {
                            _uiState.update {
                                it.copy(
                                    allUsers = result.data ?: emptyList(),
                                    isLoading = false,
                                    error = null
                                )
                            }
                            applyFilters()
                        }
                        is Resource.Error -> {
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.message
                                )
                            }
                        }
                        else -> {
                            _uiState.update { it.copy(isLoading = false) }
                        }
                    }
                }
        }
    }

    fun refreshUserData() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }

            when (val result = reloadUserUseCase()) {
                is Resource.Success -> {
                    _uiState.update {
                        it.copy(
                            currentUser = result.data,
                            isRefreshing = false
                        )
                    }
                }
                is Resource.Error -> {
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            error = result.message
                        )
                    }
                }
                else -> {
                    _uiState.update { it.copy(isRefreshing = false) }
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilters()
    }

    fun onVerificationFilterChange(filter: VerificationFilter) {
        _uiState.update { it.copy(verificationFilter = filter) }
        applyFilters()
    }

    private fun applyFilters() {
        val currentState = _uiState.value
        var filtered = currentState.allUsers

        filtered = when (currentState.verificationFilter) {
            VerificationFilter.ALL -> filtered
            VerificationFilter.VERIFIED -> filtered.filter { it.emailVerified }
            VerificationFilter.NOT_VERIFIED -> filtered.filter { !it.emailVerified }
        }

        if (currentState.searchQuery.isNotBlank()) {
            val query = currentState.searchQuery.lowercase()
            filtered = filtered.filter {
                it.name.lowercase().contains(query) ||
                        it.email.lowercase().contains(query)
            }
        }

        _uiState.update { it.copy(filteredUsers = filtered) }
    }

    fun signOut() {
        viewModelScope.launch {
            signOutUseCase()
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}