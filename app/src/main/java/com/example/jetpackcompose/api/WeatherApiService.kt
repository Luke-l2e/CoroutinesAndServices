package com.example.jetpackcompose.api

import android.util.Log
import com.example.jetpackcompose.data.ForecastData
import com.example.jetpackcompose.data.WeatherData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * A service object that handles API requests for weather data from the OpenWeatherMap API.
 * It provides methods to fetch current weather data and weather forecasts.
 */
object WeatherApiService {
    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(WeatherApi::class.java)

    /**
     * Interface that defines the endpoints for fetching weather and forecast data.
     */
    interface WeatherApi {
        /**
         * Fetches current weather data for a given city.
         *
         * @param city The name of the city for which the weather data is requested.
         * @param apiKey The API key required to authenticate the request.
         * @param units The unit of measurement for the temperature. Default is "metric".
         * @return A [retrofit2.Response] containing [WeatherData] or an error code if the request fails.
         */
        @GET("weather")
        suspend fun fetchWeather(
            @Query("q") city: String,
            @Query("appid") apiKey: String,
            @Query("units") units: String = "metric"
        ): retrofit2.Response<WeatherData>

        /**
         * Fetches weather forecast data for a given city.
         *
         * @param city The name of the city for which the forecast data is requested.
         * @param apiKey The API key required to authenticate the request.
         * @param units The unit of measurement for the temperature. Default is "metric".
         * @return A [retrofit2.Response] containing [ForecastData] or an error code if the request fails.
         */
        @GET("forecast")
        suspend fun fetchForecast(
            @Query("q") city: String,
            @Query("appid") apiKey: String,
            @Query("units") units: String = "metric"
        ): retrofit2.Response<ForecastData>
    }

    /**
     * Fetches current weather data for a specific city from the OpenWeatherMap API.
     *
     * @param city The name of the city for which the weather data is requested.
     * @param apiKey The API key required for the request.
     * @return The [WeatherData] object containing weather information, or null if the request fails.
     */
    suspend fun fetchWeather(city: String, apiKey: String): WeatherData? {
        return try {
            withContext(Dispatchers.Default) {
                val response = api.fetchWeather(city, apiKey)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("WeatherApiService", "Failed to fetch data: ${response.code()}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("WeatherApiService", "Error fetching data: ${e.message}")
            null
        }
    }

    /**
     * Fetches weather forecast data for a specific city from the OpenWeatherMap API.
     *
     * @param city The name of the city for which the forecast data is requested.
     * @param apiKey The API key required for the request.
     * @return The [ForecastData] object containing forecast information, or null if the request fails.
     */
    suspend fun fetchForecast(city: String, apiKey: String): ForecastData? {
        return try {
            withContext(Dispatchers.IO) {
                val response = api.fetchForecast(city, apiKey)
                if (response.isSuccessful) {
                    response.body()
                } else {
                    Log.e("WeatherApiService", "Failed to fetch data: ${response.code()}")
                    null
                }
            }
        } catch (e: Exception) {
            Log.e("WeatherApiService", "Error fetching data: ${e.message}")
            null
        }
    }
}
