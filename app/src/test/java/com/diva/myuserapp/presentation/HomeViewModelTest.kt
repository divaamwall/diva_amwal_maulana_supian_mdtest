package com.diva.myuserapp.presentation

import app.cash.turbine.test
import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.model.User
import com.diva.myuserapp.domain.usecase.*
import com.diva.myuserapp.presentation.home.HomeViewModel
import com.diva.myuserapp.presentation.home.VerificationFilter
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {
    private lateinit var getCurrentUserUseCase: GetCurrentUserUseCase
    private lateinit var getAllUsersUseCase: GetAllUsersUseCase
    private lateinit var reloadUserUseCase: ReloadUserUseCase
    private lateinit var signOutUseCase: SignOutUseCase
    private lateinit var viewModel: HomeViewModel

    private val testDispatcher = StandardTestDispatcher()

    private val dummyUsers = listOf(
        User("1", "test1", "test1@test.com", emailVerified = true),
        User("2", "test2", "test2@test.com", emailVerified = false)
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getCurrentUserUseCase = mockk(relaxed = true)
        getAllUsersUseCase = mockk()
        reloadUserUseCase = mockk(relaxed = true)
        signOutUseCase = mockk(relaxed = true)

        coEvery { getAllUsersUseCase() } returns flowOf(Resource.Success(dummyUsers))

        viewModel = HomeViewModel(
            getCurrentUserUseCase,
            getAllUsersUseCase,
            reloadUserUseCase,
            signOutUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadAllUsers should update allUsers and filteredUsers on success`() = runTest {
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(2, state.allUsers.size)
            assertEquals(2, state.filteredUsers.size)
            assertFalse(state.isLoading)
        }
    }

    @Test
    fun `applyFilters should filter verified users correctly`() = runTest {
        advanceUntilIdle()

        viewModel.onVerificationFilterChange(VerificationFilter.VERIFIED)

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(1, state.filteredUsers.size)
            assertTrue(state.filteredUsers[0].emailVerified)
        }
    }

    @Test
    fun `onSearchQueryChange should filter users by name`() = runTest {
        advanceUntilIdle()

        viewModel.onSearchQueryChange("test1")

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(1, state.filteredUsers.size)
            assertEquals("test1", state.filteredUsers[0].name)
        }
    }

    @Test
    fun `loadAllUsers should show error message on failure`() = runTest {
        val errorMessage = "Gagal mengambil data dari Firestore"
        coEvery { getAllUsersUseCase() } returns flowOf(Resource.Error(errorMessage))

        viewModel = HomeViewModel(getCurrentUserUseCase, getAllUsersUseCase, reloadUserUseCase, signOutUseCase)
        advanceUntilIdle()

        viewModel.uiState.test {
            val state = awaitItem()
            assertEquals(errorMessage, state.error)
            assertFalse(state.isLoading)
        }
    }
}