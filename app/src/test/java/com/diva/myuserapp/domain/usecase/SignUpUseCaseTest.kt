package com.diva.myuserapp.domain.usecase

import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.model.User
import com.diva.myuserapp.domain.repository.AuthRepository
import com.diva.myuserapp.domain.repository.UserRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class SignUpUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var signUpUseCase: SignUpUseCase

    @Before
    fun setup() {
        authRepository = mockk()
        userRepository = mockk()
        signUpUseCase = SignUpUseCase(authRepository, userRepository)
    }

    @Test
    fun `invoke with valid data returns success and saves user`() = runTest {
        // Given
        val name = "Test User"
        val email = "test@example.com"
        val password = "Test123"
        val expectedUser = User(
            uid = "123",
            name = name,
            email = email,
            emailVerified = false
        )

        coEvery { authRepository.signUp(name, email, password) } returns Resource.Success(expectedUser)
        coEvery { userRepository.saveUser(expectedUser) } returns Resource.Success(Unit)
        coEvery { authRepository.sendEmailVerification() } returns Resource.Success(Unit)

        // When
        val result = signUpUseCase(name, email, password)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(expectedUser, (result as Resource.Success).data)
        coVerify(exactly = 1) { authRepository.signUp(name, email, password) }
        coVerify(exactly = 1) { userRepository.saveUser(expectedUser) }
        coVerify(exactly = 1) { authRepository.sendEmailVerification() }
    }

    @Test
    fun `invoke with empty fields returns error`() = runTest {
        // Given
        val name = ""
        val email = "test@example.com"
        val password = "Test123"

        // When
        val result = signUpUseCase(name, email, password)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Semua kolom wajib diisi", (result as Resource.Error).message)
    }

    @Test
    fun `invoke with short name returns error`() = runTest {
        // Given
        val name = "Ab"
        val email = "test@example.com"
        val password = "Test123"

        // When
        val result = signUpUseCase(name, email, password)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Nama Minimal 3 karakter", (result as Resource.Error).message)
    }

    @Test
    fun `invoke with invalid email returns error`() = runTest {
        // Given
        val name = "Test User"
        val email = "invalid-email"
        val password = "Test123"

        // When
        val result = signUpUseCase(name, email, password)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Format email salah", (result as Resource.Error).message)
    }

    @Test
    fun `invoke returns success even if email verification fails`() = runTest {
        // Given
        val name = "Test User"
        val email = "test@example.com"
        val password = "Test123"
        val expectedUser = User("123", name, email, false)

        coEvery { authRepository.signUp(any(), any(), any()) } returns Resource.Success(expectedUser)
        coEvery { userRepository.saveUser(any()) } returns Resource.Success(Unit)
        coEvery { authRepository.sendEmailVerification() } returns Resource.Error("Gagal mengirim email")

        // When
        val result = signUpUseCase(name, email, password)

        // Then
        assertTrue(result is Resource.Success)
        coVerify { authRepository.sendEmailVerification() }
    }
}