package com.weatherapp.model.weather

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class WeatherDay {

    @SerializedName("weather")
    @Expose
    var weather: List<Weather>? = null

    @SerializedName("base")
    @Expose
    var base: String? = null

    @SerializedName("main")
    @Expose
    var main: Main? = null

    @SerializedName("dt")
    @Expose
    var dt: Long? = null

    @SerializedName("id")
    @Expose
    var id: Long? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("cod")
    @Expose
    var cod: Long? = null

}