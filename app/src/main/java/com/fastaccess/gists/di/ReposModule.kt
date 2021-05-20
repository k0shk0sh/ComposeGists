package com.fastaccess.gists.di

import com.fastaccess.gists.repos.GistRepository
import com.fastaccess.gists.repos.GistRepositoryProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ReposModule {
    @Binds
    abstract fun bindGistRepo(provider: GistRepositoryProvider): GistRepository
}