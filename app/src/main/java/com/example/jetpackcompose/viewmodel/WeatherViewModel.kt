package com.example.jetpackcompose.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jetpackcompose.api.WeatherApiService
import com.example.jetpackcompose.data.ForecastItem
import com.example.jetpackcompose.data.WeatherData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing weather and forecast data. It communicates with the
 * WeatherApiService to fetch current weather and forecast information.
 */
class WeatherViewModel : ViewModel() {

    private val _currentWeather = MutableStateFlow<WeatherData?>(null)
    val currentWeather: StateFlow<WeatherData?> = _currentWeather

    private val _forecast = MutableStateFlow<List<ForecastItem>>(emptyList())
    val forecast: StateFlow<List<ForecastItem>> = _forecast

    private val _iconUrl = MutableStateFlow<String?>(null)
    val iconUrl: StateFlow<String?> get() = _iconUrl

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> get() = _errorMessage

    /**
     * Fetches the current weather data for a specified city.
     * The data is fetched using the [WeatherApiService] and updates the corresponding state.
     *
     * @param city The name of the city for which to fetch weather data.
     * @param apiKey The API key to authenticate the request.
     */
    fun fetchWeatherData(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val weatherResponse = WeatherApiService.fetchWeather(city, apiKey)
                if (weatherResponse != null) {
                    _currentWeather.value = weatherResponse
                    fetchWeatherIcon(weatherResponse.weather.firstOrNull()?.icon.orEmpty())
                    _errorMessage.value = null
                } else {
                    _errorMessage.value =
                        "Failed to fetch weather. Please check your API key or city name."
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Fetches the weather forecast data for a specified city.
     * The data is fetched using the [WeatherApiService] and updates the corresponding state.
     *
     * @param city The name of the city for which to fetch forecast data.
     * @param apiKey The API key to authenticate the request.
     */
    fun fetchForecastData(city: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val forecastResponse = WeatherApiService.fetchForecast(city, apiKey)
                if (forecastResponse != null) {
                    _forecast.value = forecastResponse.list
                    for (forecastItem: ForecastItem in forecastResponse.list) {
                        fetchWeatherIcon(forecastItem.weather.firstOrNull()?.icon.orEmpty())
                    }
                    _errorMessage.value = null
                } else {
                    _errorMessage.value =
                        "Failed to fetch forecast. Please check your API key or city name."
                }
            } catch (e: Exception) {
                _errorMessage.value = "An error occurred: ${e.localizedMessage}"
            }
        }
    }

    /**
     * Fetches the URL of the weather icon using the icon ID.
     * The URL is constructed using the OpenWeatherMap icon base URL.
     *
     * @param iconId The icon ID that corresponds to the weather condition.
     */
    private fun fetchWeatherIcon(iconId: String) {
        if (iconId.isNotEmpty()) {
            _iconUrl.value = "https://openweathermap.org/img/wn/$iconId@2x.png"
        }
    }
}
