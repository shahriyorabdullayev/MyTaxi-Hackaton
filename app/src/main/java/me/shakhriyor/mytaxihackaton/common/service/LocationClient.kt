package me.shakhriyor.mytaxihackaton.common.service

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    fun getLocationUpdates(interval: Long): Flow<Location>

    fun getLocation(): Flow<Location>

    class LocationException(message: String): Exception()
}