package me.shakhriyor.mytaxihackaton.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.shakhriyor.mytaxihackaton.common.Resource
import me.shakhriyor.mytaxihackaton.data.model.CurrentLocation
import me.shakhriyor.mytaxihackaton.domain.use_cases.GetCurrentLocationUseCase
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase
): ViewModel() {

    private val _currentLocation = MutableStateFlow<Resource<List<CurrentLocation>>>(Resource.EMPTY)
    val currentLocation = _currentLocation


    fun getCurrentLocation() = viewModelScope.launch {
        _currentLocation.value = Resource.LOADING
        _currentLocation.value = getCurrentLocationUseCase()
    }

}