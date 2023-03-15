package me.shakhriyor.mytaxihackaton.data.source

import me.shakhriyor.mytaxihackaton.data.db.CurrentLocationDao
import me.shakhriyor.mytaxihackaton.data.model.CurrentLocation
import me.shakhriyor.mytaxihackaton.domain.datasource.LocalDataSource
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val currentLocationDao: CurrentLocationDao
): LocalDataSource {

    override suspend fun insertCurrentLocation(currentLocation: CurrentLocation) {
        currentLocationDao.insertCurrentLocation(currentLocation)
    }

    override suspend fun updateCurrentLocation(currentLocation: CurrentLocation) {
        currentLocationDao.updateCurrentLocation(currentLocation)
    }

    override suspend fun getCurrentLocations(): List<CurrentLocation> {
        return currentLocationDao.getCurrentLocations()
    }

    override suspend fun deleteCurrentLocations() {
        currentLocationDao.deleteCurrentLocations()
    }

}