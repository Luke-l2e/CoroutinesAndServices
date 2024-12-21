package com.example.jetpackcompose.data

/**
 * Represents the weather data returned by the OpenWeatherMap API for a specific city.
 *
 * @param coord Coordinates (longitude and latitude) of the city.
 * @param weather A list of [Weather] objects containing weather conditions for the city.
 * @param base The base of the weather data, usually a station.
 * @param main An object containing main weather details like temperature, pressure, and humidity.
 * @param visibility Visibility (in meters) for the city.
 * @param wind An object containing wind speed, direction, and gust data.
 * @param clouds An object containing cloud coverage data.
 * @param dt The date and time of the weather data, in Unix timestamp format.
 * @param sys System-related information for the city, including sunrise and sunset times.
 * @param timezone The timezone offset (in seconds) for the city.
 * @param id The city ID.
 * @param name The name of the city.
 * @param cod The status code for the weather data request.
 */
data class WeatherData(
    val coord: Coord,
    val weather: List<Weather>,
    val base: String,
    val main: Main,
    val visibility: Int,
    val wind: Wind,
    val clouds: Clouds,
    val dt: Long,
    val sys: Sys,
    val timezone: Int,
    val id: Int,
    val name: String,
    val cod: Int
)

/**
 * Represents the coordinates (longitude and latitude) of the city.
 *
 * @param lon The longitude of the city.
 * @param lat The latitude of the city.
 */
data class Coord(val lon: Double, val lat: Double)

/**
 * Represents weather conditions for a specific city.
 *
 * @param id The unique ID for the weather condition.
 * @param main The main weather condition (e.g., "Clear", "Rain").
 * @param description A description of the weather condition (e.g., "clear sky", "light rain").
 * @param icon An icon code representing the weather condition.
 */
data class Weather(val id: Int, val main: String, val description: String, val icon: String)

/**
 * Represents the main weather details for a specific city.
 *
 * @param temp The current temperature in Celsius.
 * @param feels_like The temperature as it feels to humans, considering factors like humidity and wind.
 * @param temp_min The minimum temperature at the moment in Celsius.
 * @param temp_max The maximum temperature at the moment in Celsius.
 * @param pressure The atmospheric pressure at the moment, in hPa (hectopascals).
 * @param humidity The humidity percentage at the moment.
 */
data class Main(
    val temp: Double,
    val feels_like: Double,
    val temp_min: Double,
    val temp_max: Double,
    val pressure: Int,
    val humidity: Int
)

/**
 * Represents the wind conditions for a specific city.
 *
 * @param speed The wind speed in meters per second.
 * @param deg The wind direction, measured in degrees.
 * @param gust The gust speed, in meters per second, if available.
 */
data class Wind(val speed: Double, val deg: Int, val gust: Double)

/**
 * Represents cloud coverage for a specific city.
 *
 * @param all The percentage of cloud coverage (0 to 100).
 */
data class Clouds(val all: Int)

/**
 * Represents system-related information for a specific city.
 *
 * @param type The internal type code for the weather data.
 * @param id The internal ID for the system.
 * @param country The country code for the city.
 * @param sunrise The Unix timestamp for the time of sunrise.
 * @param sunset The Unix timestamp for the time of sunset.
 */
data class Sys(
    val type: Int,
    val id: Int,
    val country: String,
    val sunrise: Long,
    val sunset: Long
)
