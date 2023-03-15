package me.shakhriyor.mytaxihackaton.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import me.shakhriyor.mytaxihackaton.data.model.CurrentLocation

@Database(entities = [CurrentLocation::class], version = 2, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {

    abstract fun currentLocationDao(): CurrentLocationDao
}