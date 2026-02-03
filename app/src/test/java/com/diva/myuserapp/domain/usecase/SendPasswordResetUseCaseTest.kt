package com.diva.myuserapp.domain.usecase

import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.repository.AuthRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SendPasswordResetUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var sendPasswordResetUseCase: SendPasswordResetUseCase

    @Before
    fun setup() {
        authRepository = mockk()
        sendPasswordResetUseCase = SendPasswordResetUseCase(authRepository)
    }

    @Test
    fun `invoke with valid email returns success`() = runTest {
        // Given
        val email = "test@example.com"
        coEvery { authRepository.sendPasswordResetEmail(email) } returns Resource.Success(Unit)

        // When
        val result = sendPasswordResetUseCase(email)

        // Then
        assertTrue(result is Resource.Success)
        coVerify(exactly = 1) { authRepository.sendPasswordResetEmail(email) }
    }

    @Test
    fun `invoke with empty email returns error`() = runTest {
        // Given
        val email = ""

        // When
        val result = sendPasswordResetUseCase(email)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Email tidak boleh kosong", (result as Resource.Error).message)
        coVerify(exactly = 0) { authRepository.sendPasswordResetEmail(any()) }
    }

    @Test
    fun `invoke with invalid email format returns error`() = runTest {
        // Given
        val email = "invalid-email-format"

        // When
        val result = sendPasswordResetUseCase(email)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Format email salah", (result as Resource.Error).message)
        coVerify(exactly = 0) { authRepository.sendPasswordResetEmail(any()) }
    }

    @Test
    fun `invoke when repository returns error should return error message`() = runTest {
        // Given
        val email = "test@example.com"
        val errorMessage = "User tidak ditemukan"
        coEvery { authRepository.sendPasswordResetEmail(email) } returns Resource.Error(errorMessage)

        // When
        val result = sendPasswordResetUseCase(email)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals(errorMessage, (result as Resource.Error).message)
    }
}