package me.shakhriyor.mytaxihackaton.domain.use_cases

import me.shakhriyor.mytaxihackaton.data.model.CurrentLocation
import me.shakhriyor.mytaxihackaton.domain.repository.MainRepository
import javax.inject.Inject

class UpdateLocationUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(currentLocation: CurrentLocation) {
        mainRepository.updateCurrentLocation(currentLocation)
    }
}