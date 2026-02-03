package com.diva.myuserapp.presentation

import app.cash.turbine.test
import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.usecase.SendPasswordResetUseCase
import com.diva.myuserapp.presentation.auth.forgot_password.ForgotPasswordViewModel
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
class ForgotPasswordViewModelTest {
    private lateinit var sendPasswordResetUseCase: SendPasswordResetUseCase
    private lateinit var viewModel: ForgotPasswordViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        sendPasswordResetUseCase = mockk()
        viewModel = ForgotPasswordViewModel(sendPasswordResetUseCase)
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
            assertFalse(state.isLoading)
            assertFalse(state.isSuccess)
            assertNull(state.error)
        }
    }

    @Test
    fun `onEmailChange updates email state and clears error`() = runTest {
        val email = "test@example.com"

        viewModel.onEmailChange(email)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(email, state.email)
            assertNull(state.error)
        }
    }

    @Test
    fun `sendPasswordReset with valid email shows success`() = runTest {
        // Given
        val email = "test@example.com"
        viewModel.onEmailChange(email)
        coEvery { sendPasswordResetUseCase(email) } returns Resource.Success(Unit)

        // When
        viewModel.sendPasswordReset()
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
    fun `sendPasswordReset with error shows error message`() = runTest {
        // Given
        val email = "unregistered@example.com"
        val errorMessage = "Email tidak ditemukan"
        viewModel.onEmailChange(email)
        coEvery { sendPasswordResetUseCase(email) } returns Resource.Error(errorMessage)

        // When
        viewModel.sendPasswordReset()
        advanceUntilIdle()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isLoading)
            assertFalse(state.isSuccess)
            assertEquals(errorMessage, state.error)
        }
    }

    @Test
    fun `resetSuccessState sets isSuccess to false`() = runTest {
        // Given
        val email = "test@example.com"
        viewModel.onEmailChange(email)
        coEvery { sendPasswordResetUseCase(email) } returns Resource.Success(Unit)
        viewModel.sendPasswordReset()
        advanceUntilIdle()

        // When
        viewModel.resetSuccessState()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertFalse(state.isSuccess)
        }
    }

    @Test
    fun `clearError sets error to null`() = runTest {
        // Given
        coEvery { sendPasswordResetUseCase(any()) } returns Resource.Error("Error")
        viewModel.sendPasswordReset()
        advanceUntilIdle()

        // When
        viewModel.clearError()

        // Then
        viewModel.uiState.test {
            val state = awaitItem()
            assertNull(state.error)
        }
    }
}