package me.shakhriyor.mytaxihackaton.domain.repository

import kotlinx.coroutines.flow.Flow
import me.shakhriyor.mytaxihackaton.data.model.CurrentLocation

interface MainRepository {

    suspend fun insertCurrentLocation(currentLocation: CurrentLocation)

    suspend fun updateCurrentLocation(currentLocation: CurrentLocation)

    suspend fun getCurrentLocations(): List<CurrentLocation>

    suspend fun deleteCurrentLocations()

}