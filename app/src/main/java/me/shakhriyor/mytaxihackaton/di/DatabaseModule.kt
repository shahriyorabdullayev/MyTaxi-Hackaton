package me.shakhriyor.mytaxihackaton.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.shakhriyor.mytaxihackaton.data.db.AppDatabase
import me.shakhriyor.mytaxihackaton.data.db.CurrentLocationDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideLocationRoomDB(@ApplicationContext appContext: Context): AppDatabase =
        Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "current_location.db"
        ).fallbackToDestructiveMigration().build()


    @Provides
    @Singleton
    fun provideCurrentLocationDao(appDatabase: AppDatabase): CurrentLocationDao {
        return appDatabase.currentLocationDao()
    }
}