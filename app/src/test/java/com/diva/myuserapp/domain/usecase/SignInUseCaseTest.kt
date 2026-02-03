package com.diva.myuserapp.domain.usecase

import com.diva.myuserapp.domain.repository.AuthRepository
import com.diva.myuserapp.domain.repository.UserRepository
import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.model.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test


class SignInUseCaseTest {
    private lateinit var authRepository: AuthRepository
    private lateinit var userRepository: UserRepository
    private lateinit var signInUseCase: SignInUseCase

    @Before
    fun setup() {
        authRepository = mockk()
        userRepository = mockk(relaxed = true)
        signInUseCase = SignInUseCase(authRepository, userRepository)
    }

    @Test
    fun `invoke with valid credentials returns success`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "Test123"
        val expectedUser = User(
            uid = "123",
            name = "Test User",
            email = email,
            emailVerified = true
        )
        coEvery { authRepository.signIn(email, password) } returns Resource.Success(expectedUser)

        // When
        val result = signInUseCase(email, password)

        // Then
        assertTrue(result is Resource.Success)
        assertEquals(expectedUser, (result as Resource.Success).data)
        coVerify(exactly = 1) { authRepository.signIn(email, password) }
        coVerify(exactly = 1) { userRepository.updateUser(expectedUser) }
    }

    @Test
    fun `invoke with empty email returns error`() = runTest {
        // Given
        val email = ""
        val password = "Test123"

        // When
        val result = signInUseCase(email, password)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Email dan password tidak boleh kosong", (result as Resource.Error).message)
        coVerify(exactly = 0) { authRepository.signIn(any(), any()) }
    }

    @Test
    fun `invoke with empty password returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = ""

        // When
        val result = signInUseCase(email, password)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Email dan password tidak boleh kosong", (result as Resource.Error).message)
    }

    @Test
    fun `invoke with invalid email format returns error`() = runTest {
        // Given
        val email = "invalid-email"
        val password = "Test123"

        // When
        val result = signInUseCase(email, password)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Format email salah", (result as Resource.Error).message)
    }

    @Test
    fun `invoke with short password returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "12345"

        // When
        val result = signInUseCase(email, password)

        // Then
        assertTrue(result is Resource.Error)
        assertEquals("Password seharusnya 6 karakter atau lebih", (result as Resource.Error).message)
    }

    @Test
    fun `invoke with repository error returns error`() = runTest {
        // Given
        val email = "test@example.com"
        val password = "Test123"
        coEvery { authRepository.signIn(email, password) } returns Resource.Error("Login gagal")

        // When
        val result = signInUseCase(email, password)

        // Then
        assertTrue(result is Resource.Error)
        coVerify(exactly = 1) { authRepository.signIn(email, password) }
    }
}