package com.example.githubuser.di

import com.example.githubuser.repository.UserRepository
import com.example.githubuser.storage.AppDataStorage
import com.example.githubuser.usecase.UserUseCase
import com.example.githubuser.usecase.UserUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UseCaseModule {

    @Provides
    @Singleton
    fun provideUserUseCase(
        userRepository: UserRepository,
        appDataStorage: AppDataStorage
    ): UserUseCase {
        return UserUseCaseImpl(userRepository, appDataStorage)
    }
}