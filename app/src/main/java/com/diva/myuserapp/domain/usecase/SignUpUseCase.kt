package com.diva.myuserapp.domain.usecase

import com.diva.myuserapp.data.Resource
import com.diva.myuserapp.domain.model.User
import com.diva.myuserapp.domain.repository.AuthRepository
import com.diva.myuserapp.domain.repository.UserRepository
import com.diva.myuserapp.utils.EmailValidator
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        password: String
    ): Resource<User> {
        // Validation
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            return Resource.Error("Semua kolom wajib diisi")
        }

        if (name.length < 3) {
            return Resource.Error("Nama Minimal 3 karakter")
        }

        if (!EmailValidator.isValid(email)) {
            return Resource.Error("Format email salah")
        }

        if (password.length < 6) {
            return Resource.Error("Password minimal 6 karakter")
        }

        // Register
        return when (val result = authRepository.signUp(name, email, password)) {
            is Resource.Success -> {
                result.data?.let { user ->
                    when (userRepository.saveUser(user)) {
                        is Resource.Success -> {
                            authRepository.sendEmailVerification()
                            Resource.Success(user)
                        }
                        is Resource.Error -> Resource.Error("Gagal menyimpan data pengguna")
                        else -> Resource.Error("Terjadi masalah saat menyimpan user")
                    }
                } ?: Resource.Error("Register berhasil dilakukan tetapi data user kosong")
            }
            is Resource.Error -> Resource.Error(result.message ?: "Register gagal")
            is Resource.Loading -> Resource.Error("Terjadi permasalahan sistem")
        }
    }
}