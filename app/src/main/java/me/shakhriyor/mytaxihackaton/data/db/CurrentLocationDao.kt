package me.shakhriyor.mytaxihackaton.data.db

import androidx.room.*
import me.shakhriyor.mytaxihackaton.data.model.CurrentLocation

@Dao
interface CurrentLocationDao {

    @Transaction
    suspend fun updateCurrentLocation(currentLocation: CurrentLocation) {
        currentLocation.let {
            deleteCurrentLocations()
            insertCurrentLocation(it)
        }
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCurrentLocation(currentLocation: CurrentLocation)

    @Query("DELETE FROM location")
    suspend fun deleteCurrentLocations()


    @Query("SELECT * FROM location ORDER BY time")
    suspend fun getCurrentLocations(): List<CurrentLocation>

}