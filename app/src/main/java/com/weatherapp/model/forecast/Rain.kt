package com.weatherapp.model.forecast

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Rain {
    @SerializedName("3h")
    @Expose
    var h3: Float? = 0f
}
