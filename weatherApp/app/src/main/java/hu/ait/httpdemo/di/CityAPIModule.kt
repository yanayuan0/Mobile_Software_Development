package hu.ait.httpdemo.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hu.ait.httpdemo.network.CityAPI
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object CityAPIModule {

    @Provides
    @Singleton
    @CityRetrofit
    fun provideCityRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/")
            .addConverterFactory(
                Json { ignoreUnknownKeys = true }.asConverterFactory("application/json".toMediaType())
            )
            .client(client)
            .build()
    }

    @Provides
    @Singleton
    fun provideCityAPI(@CityRetrofit cityRetrofit: Retrofit): CityAPI {
        return cityRetrofit.create(CityAPI::class.java)
    }
}
