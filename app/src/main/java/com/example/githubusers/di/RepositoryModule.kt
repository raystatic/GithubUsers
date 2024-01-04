package com.example.githubusers.di

import com.example.githubusers.data.repositories.GithubUsersRepositoryImpl
import com.example.githubusers.domain.repositories.GithubUsersRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by Rahul Ray.
 * Version 1.0.0 on 04,January,2024
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideGithubUserRepository(
        repository: GithubUsersRepositoryImpl
    ):GithubUsersRepository = repository

}