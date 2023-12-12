package com.weatherapp.backend.configuration

import com.weatherapp.model.forecast.Forecast
import com.weatherapp.model.weather.WeatherDay
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Query

interface ApiInterface<T> {

    // to get  current weather data depend on location
    @GET("weather")
    fun getCurrentDayWeather(
        @HeaderMap headers: HashMap<String, String>?,
        @Query("appid") appId: String?,
        @Query("lat") lat: Double, @Query("lon") lon: Double
    ): Call<WeatherDay?>?

    // to get  5 day / 3 hour forecast data depend on location
    @GET("forecast")
    fun getForecast(
        @HeaderMap headers: HashMap<String, String>?,
        @Query("appid") appId: String?,
        @Query("lat") lat: Double, @Query("lon") lon: Double
    ): Call<Forecast?>?

}