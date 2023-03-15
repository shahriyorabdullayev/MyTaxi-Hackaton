package me.shakhriyor.mytaxihackaton.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.shakhriyor.mytaxihackaton.data.repository.MainRepositoryImpl
import me.shakhriyor.mytaxihackaton.domain.datasource.LocalDataSource
import me.shakhriyor.mytaxihackaton.domain.repository.MainRepository
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideMainRepository(
        localDataSource: LocalDataSource
    ): MainRepository = MainRepositoryImpl(localDataSource)

}