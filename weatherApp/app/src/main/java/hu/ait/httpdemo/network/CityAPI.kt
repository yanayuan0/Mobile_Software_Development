package hu.ait.httpdemo.network

import hu.ait.httpdemo.data.city.CityResultItem
import hu.ait.httpdemo.data.weather.WeatherResult
import retrofit2.http.GET
import retrofit2.http.Query

interface CityAPI {
    @GET("geo/1.0/direct")
    suspend fun getCityDetails(
        @Query("q") cityName: String,
        @Query("limit") limit: Int = 1,
        @Query("appid") apiKey: String,
    ): List<CityResultItem>

    @GET("data/2.5/weather")
    suspend fun getWeatherData(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String
    ): WeatherResult
}
