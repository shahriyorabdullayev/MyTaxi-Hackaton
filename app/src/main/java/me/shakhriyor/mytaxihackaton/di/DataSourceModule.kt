package me.shakhriyor.mytaxihackaton.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.shakhriyor.mytaxihackaton.data.source.LocalDataSourceImpl
import me.shakhriyor.mytaxihackaton.data.db.CurrentLocationDao
import me.shakhriyor.mytaxihackaton.domain.datasource.LocalDataSource
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideLocalDataSource(
        currentLocationDao: CurrentLocationDao
    ): LocalDataSource = LocalDataSourceImpl(currentLocationDao)

}