package com.example.jetpackcompose.data

/**
 * Represents the forecast data returned by the OpenWeatherMap API.
 *
 * @param cod The internal code used by OpenWeatherMap API to denote the response status.
 * @param message A message that accompanies the API response (if any).
 * @param cnt The number of forecast data items returned.
 * @param list A list of [ForecastItem] objects representing individual forecast entries.
 */
data class ForecastData(
    val cod: String,
    val message: Int,
    val cnt: Int,
    val list: List<ForecastItem>
)

/**
 * Represents an individual forecast item containing weather data for a specific time.
 *
 * @param dt The date and time of the forecast, in Unix timestamp format.
 * @param main An object containing the main weather details, such as temperature and humidity.
 * @param weather A list of [Weather] objects providing detailed weather conditions.
 * @param clouds An object containing cloudiness data for the forecast.
 * @param wind An object containing wind speed and direction for the forecast.
 * @param visibility The visibility (in meters) for the forecast time.
 * @param pop Probability of precipitation, ranging from 0 to 1.
 * @param sys The system-related data for the forecast.
 * @param dt_txt A human-readable string representing the date and time of the forecast.
 * @param rain An optional [Rain] object containing rain data, if applicable.
 */
data class ForecastItem(
    val dt: Long,
    val main: Main,
    val weather: List<Weather>,
    val clouds: Clouds,
    val wind: Wind,
    val visibility: Int,
    val pop: Double,
    val sys: ForecastSys,
    val dt_txt: String,
    val rain: Rain? = null
)

/**
 * Represents system-related information for a weather forecast item.
 *
 * @param pod The part of the day (e.g., "d" for day, "n" for night).
 */
data class ForecastSys(val pod: String)

/**
 * Represents rain data for a weather forecast item, specifically for the last 3 hours.
 *
 * @param `3h` The amount of rain (in mm) that fell in the last 3 hours, if available.
 */
data class Rain(val `3h`: Double? = null)
