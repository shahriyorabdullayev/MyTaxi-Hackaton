package me.shakhriyor.mytaxihackaton.data.repository

import me.shakhriyor.mytaxihackaton.data.model.CurrentLocation
import me.shakhriyor.mytaxihackaton.domain.datasource.LocalDataSource
import me.shakhriyor.mytaxihackaton.domain.repository.MainRepository
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
): MainRepository {

    override suspend fun insertCurrentLocation(currentLocation: CurrentLocation) {
        localDataSource.insertCurrentLocation(currentLocation)
    }

    override suspend fun updateCurrentLocation(currentLocation: CurrentLocation) {
        localDataSource.updateCurrentLocation(currentLocation)
    }

    override suspend fun getCurrentLocations(): List<CurrentLocation> {
        return localDataSource.getCurrentLocations()
    }

    override suspend fun deleteCurrentLocations() {
        localDataSource.deleteCurrentLocations()
    }


}