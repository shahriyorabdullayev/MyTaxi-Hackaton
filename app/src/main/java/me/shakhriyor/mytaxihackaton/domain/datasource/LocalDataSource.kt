package me.shakhriyor.mytaxihackaton.domain.datasource

import kotlinx.coroutines.flow.Flow
import me.shakhriyor.mytaxihackaton.data.model.CurrentLocation


interface LocalDataSource {

    suspend fun insertCurrentLocation(currentLocation: CurrentLocation)

    suspend fun updateCurrentLocation(currentLocation: CurrentLocation)

    suspend fun getCurrentLocations(): List<CurrentLocation>

    suspend fun deleteCurrentLocations()

}