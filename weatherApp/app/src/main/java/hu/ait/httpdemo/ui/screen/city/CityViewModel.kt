package hu.ait.httpdemo.ui.screen.city

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.ait.httpdemo.data.weather.WeatherResult
import hu.ait.httpdemo.network.CityAPI
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CityViewModel @Inject constructor(
    private val cityAPI: CityAPI
) : ViewModel() {

    var cityUiStateMap = mutableStateMapOf<String, CityUiState>()
        private set

    fun getCityCoordinates(cityName: String) {
        cityUiStateMap[cityName] = CityUiState(cityDetails = CityDetailsUiState.Loading)
        viewModelScope.launch {
            val cityDetailsUiState = try {
                val result = cityAPI.getCityDetails(cityName, apiKey = "3eb84fbf5fcc43375c529e5614dee867")
                if (result.isNotEmpty()) {
                    CityDetailsUiState.Success(result[0].lat, result[0].lon)
                } else {
                    CityDetailsUiState.Error("City not found")
                }
            } catch (e: Exception) {
                CityDetailsUiState.Error(e.localizedMessage ?: "Unknown error")
            }

            val weatherUiState = cityUiStateMap[cityName]?.weatherDetails ?: WeatherUiState.Empty

            cityUiStateMap[cityName] = CityUiState(
                cityDetails = cityDetailsUiState,
                weatherDetails = weatherUiState
            )

            if (cityDetailsUiState is CityDetailsUiState.Success) {
                getWeatherData(cityName, cityDetailsUiState.lat, cityDetailsUiState.lon)
            }
        }
    }

    fun getWeatherData(cityName: String, lat: Double?, lon: Double?) {
        if (lat != null && lon != null) {
            val currentCityUiState = cityUiStateMap[cityName]
            cityUiStateMap[cityName] = currentCityUiState?.copy(
                weatherDetails = WeatherUiState.Loading
            ) ?: CityUiState(
                cityDetails = CityDetailsUiState.Empty,
                weatherDetails = WeatherUiState.Loading
            )

            viewModelScope.launch {
                val weatherUiState = try {
                    val weatherData = cityAPI.getWeatherData(lat, lon, apiKey = "3eb84fbf5fcc43375c529e5614dee867")
                    WeatherUiState.Success(weatherData)
                } catch (e: Exception) {
                    WeatherUiState.Error(e.localizedMessage ?: "Unknown error")
                }

                val updatedCityUiState = cityUiStateMap[cityName]?.copy(
                    weatherDetails = weatherUiState
                )
                if (updatedCityUiState != null) {
                    cityUiStateMap[cityName] = updatedCityUiState
                }
            }
        } else {
            val currentCityUiState = cityUiStateMap[cityName]
            cityUiStateMap[cityName] = currentCityUiState?.copy(
                weatherDetails = WeatherUiState.Error("Invalid coordinates")
            ) ?: CityUiState(
                cityDetails = CityDetailsUiState.Empty,
                weatherDetails = WeatherUiState.Error("Invalid coordinates")
            )
        }
    }
}

sealed interface CityDetailsUiState {
    object Empty : CityDetailsUiState
    object Loading : CityDetailsUiState
    data class Success(val lat: Double?, val lon: Double?) : CityDetailsUiState
    data class Error(val message: String) : CityDetailsUiState
}

sealed interface WeatherUiState {
    object Empty : WeatherUiState
    object Loading : WeatherUiState
    data class Success(val weatherData: WeatherResult) : WeatherUiState
    data class Error(val message: String) : WeatherUiState
}

data class CityUiState(
    val cityDetails: CityDetailsUiState,
    val weatherDetails: WeatherUiState = WeatherUiState.Empty
)
