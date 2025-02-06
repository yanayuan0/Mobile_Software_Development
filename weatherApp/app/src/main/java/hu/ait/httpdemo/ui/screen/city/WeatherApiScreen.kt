package hu.ait.httpdemo.ui.screen.weather

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import hu.ait.httpdemo.data.weather.WeatherResult
import hu.ait.httpdemo.ui.screen.city.CityDetailsUiState
import hu.ait.httpdemo.ui.screen.city.CityUiState
import hu.ait.httpdemo.ui.screen.city.CityViewModel
import hu.ait.httpdemo.ui.screen.city.WeatherUiState

@Composable
fun WeatherApiScreen(
    navController: NavController,
    cityName: String,
    cityViewModel: CityViewModel = hiltViewModel()
) {
    LaunchedEffect(cityName) {
        if (!cityViewModel.cityUiStateMap.containsKey(cityName)) {
            cityViewModel.getCityCoordinates(cityName)
        }
    }

    val cityUiState = cityViewModel.cityUiStateMap[cityName]

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.Start)
        ) {
            Text(text = "Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        cityUiState?.let {
            when (it.cityDetails) {
                is CityDetailsUiState.Loading -> CircularProgressIndicator()
                is CityDetailsUiState.Success -> WeatherResultScreen(it.weatherDetails)
                is CityDetailsUiState.Error -> Text(
                    text = (it.cityDetails as CityDetailsUiState.Error).message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
                else -> Text("City details unavailable")
            }
        } ?: Text("Loading city data...")
    }
}

@Composable
fun WeatherResultScreen(weatherUiState: WeatherUiState) {
    when (weatherUiState) {
        is WeatherUiState.Loading -> CircularProgressIndicator()
        is WeatherUiState.Success -> WeatherDataScreen(weatherUiState.weatherData)
        is WeatherUiState.Error -> Text(
            text = "Error: ${weatherUiState.message}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.error
        )
        else -> Text("No weather data available")
    }
}

@Composable
fun WeatherDataScreen(weatherData: WeatherResult) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
//        Text(text = "Coordinates: ${weatherData.coord?.lat}, ${weatherData.coord?.lon}")
        Text(text = "Weather: ${weatherData.weather?.firstOrNull()?.main ?: "N/A"}")
        Text(text = "Description: ${weatherData.weather?.firstOrNull()?.description ?: "N/A"}")
        val tempInCelsius = weatherData.main?.temp?.let { it - 273.15 }
        Text(text = "Temperature: ${tempInCelsius?.let { "%.2f".format(it) } ?: "N/A"} °C")
        val tempFeelsLike = weatherData.main?.feelsLike?.let { it - 273.15 }
        Text(text = "Feels Like: ${tempFeelsLike?.let { "%.2f".format(it) } ?: "N/A"} °C")
        val tempMin = weatherData.main?.tempMin?.let { it - 273.15 }
        Text(text = "Min Temperature: ${tempMin?.let { "%.2f".format(it) } ?: "N/A"} °C")
        val tempMax = weatherData.main?.tempMax?.let { it - 273.15 }
        Text(text = "Max Temperature: ${tempMax?.let { "%.2f".format(it) } ?: "N/A"} °C")
        Text(text = "Pressure: ${weatherData.main?.pressure}")
        Text(text = "Humidity: ${weatherData.main?.humidity}")
        Text(text = "Visibility: ${weatherData.visibility}")
        Text(text = "Cloudiness: ${weatherData.clouds?.all}")
        Text(text = "Country: ${weatherData.sys?.country}")
    }
}
