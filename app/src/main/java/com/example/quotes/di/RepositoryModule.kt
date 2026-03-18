package com.example.quotes.di

import com.example.quotes.data.repository.CollectionRepositoryImpl
import com.example.quotes.data.repository.QuoteRepositoryImpl
import com.example.quotes.domain.repository.CollectionRepository
import com.example.quotes.domain.repository.QuoteRepository
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
    abstract fun bindQuoteRepository(repositoryImpl: QuoteRepositoryImpl): QuoteRepository

    @Binds
    @Singleton
    abstract fun bindCollectionRepository(repositoryImpl: CollectionRepositoryImpl): CollectionRepository
}
