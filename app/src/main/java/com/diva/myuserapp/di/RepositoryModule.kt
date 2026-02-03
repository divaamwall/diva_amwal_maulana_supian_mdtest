package com.diva.myuserapp.di

import com.diva.myuserapp.data.source.repository.AuthRepositoryImpl
import com.diva.myuserapp.data.source.repository.UserRepositoryImpl
import com.diva.myuserapp.domain.repository.AuthRepository
import com.diva.myuserapp.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository
}