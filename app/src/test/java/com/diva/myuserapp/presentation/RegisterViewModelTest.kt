package com.diva.myuserapp.presentation

import app.cash.turbine.test
import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.model.User
import com.diva.myuserapp.domain.usecase.SignUpUseCase
import com.diva.myuserapp.presentation.auth.register.RegisterViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RegisterViewModelTest {
    private lateinit var signUpUseCase: SignUpUseCase
    private lateinit var viewModel: RegisterViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        signUpUseCase = mockk()
        viewModel = RegisterViewModel(signUpUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("", state.name)
            assertEquals("", state.email)
            assertEquals("", state.password)
            assertEquals("", state.confirmPassword)
            assertFalse(state.isLoading)
            assertNull(state.error)
            assertFalse(state.isSuccess)
        }
    }

    @Test
    fun `onNameChange updates name and clears error`() = runTest {
        val name = "Diva"
        viewModel.onNameChange(name)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(name, state.name)
            assertNull(state.error)
        }
    }

    @Test
    fun `signUp with valid data shows success`() = runTest {
        // Given
        val name = "Test"
        val email = "test@example.com"
        val password = "Test123"

        val user = User(uid = "123", name = "Test", email = email, emailVerified = true)

        viewModel.onNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.onConfirmPasswordChange(password)

        coEvery { signUpUseCase(name, email, password) } returns Resource.Success(user)

        // When
        viewModel.signUp()
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertTrue(state.isSuccess)
            assertNull(state.error)
        }
    }

    @Test
    fun `signUp with error returns error message`() = runTest {
        // Given
        val name = "Test"
        val email = "test@example.com"
        val password = "wrong"
        val errorMessage = "Silakan isi kolom dengan benar"

        viewModel.onNameChange(name)
        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)
        viewModel.onConfirmPasswordChange(password)

        coEvery { signUpUseCase(name, email, password) } returns Resource.Error(errorMessage)

        // When
        viewModel.signUp()
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertFalse(state.isSuccess)
            assertEquals(errorMessage, state.error)
        }
    }
}