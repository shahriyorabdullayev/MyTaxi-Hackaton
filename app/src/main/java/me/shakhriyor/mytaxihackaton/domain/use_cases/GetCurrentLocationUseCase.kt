package me.shakhriyor.mytaxihackaton.domain.use_cases

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import me.shakhriyor.mytaxihackaton.common.Resource
import me.shakhriyor.mytaxihackaton.data.model.CurrentLocation
import me.shakhriyor.mytaxihackaton.domain.repository.MainRepository
import java.io.IOException
import java.net.HttpRetryException
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val mainRepository: MainRepository
) {

    suspend operator fun invoke(): Resource<List<CurrentLocation>> =
        try {
            Resource.SUCCESS(mainRepository.getCurrentLocations())
        } catch (e: IOException) {
            Resource.ERROR(e.localizedMessage)
        }
    }

