package com.diva.myuserapp.presentation

import app.cash.turbine.test
import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.model.User
import com.diva.myuserapp.domain.usecase.SignInUseCase
import com.diva.myuserapp.presentation.auth.login.LoginViewModel
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
class LoginViewModelTest {
    private lateinit var signInUseCase: SignInUseCase
    private lateinit var viewModel: LoginViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        signInUseCase = mockk()
        viewModel = LoginViewModel(signInUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is correct`() = runTest {
        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals("", state.email)
            assertEquals("", state.password)
            assertFalse(state.isLoading)
            assertNull(state.error)
            assertFalse(state.isSuccess)
        }
    }

    @Test
    fun `onEmailChange updates email and clears error`() = runTest {
        val email = "test@example.com"

        viewModel.onEmailChange(email)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(email, state.email)
            assertNull(state.error)
        }
    }

    @Test
    fun `signIn with valid credentials shows success`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "Test123"
        val user = User(uid = "123", name = "Test", email = email, emailVerified = true)

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        coEvery { signInUseCase(email, password) } returns Resource.Success(user)

        // When
        viewModel.signIn()
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
    fun `signIn with error shows error message`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "wrong"
        val errorMessage = "Login gagal"

        viewModel.onEmailChange(email)
        viewModel.onPasswordChange(password)

        coEvery { signInUseCase(email, password) } returns Resource.Error(errorMessage)

        // When
        viewModel.signIn()
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