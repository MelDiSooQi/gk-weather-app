package com.weatherapp.handler

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.text.Html
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.weatherapp.R
import com.weatherapp.view.CustomDialog
import java.sql.Timestamp
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Suppress("DEPRECATION")
@SuppressLint("SimpleDateFormat")
object Utilities {
    fun celsiusToFahrenheit(degree: Double): Double {
        return degree * (5 / 9).toDouble() + 32
    }

    fun fahrenheitToCelsius(degree: Double): Double {
        return (degree - 32) * (5 / 9).toDouble()
    }

    fun kelvinToCelsius(degree: Double): Double {
        return degree - 273.15
    }

    fun timestamp2Hour(timestamp: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp * 1000
        return cal[Calendar.HOUR_OF_DAY]
    }
    fun timestamp2Day(timestamp: Long): Int {
        val cal = Calendar.getInstance()
        cal.timeInMillis = timestamp * 1000
        return cal[Calendar.DAY_OF_MONTH]
    }

    fun timestamp2StringDateTime(timestamp: Long): String {
        val stamp = Timestamp(timestamp)
        val date = Date(stamp.time)
        val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm")
        return formatter.format(date)
    }

    fun timestamp2StringDate(timestamp: Long): String {
        val stamp = Timestamp(timestamp * 1000)
        val date = Date(stamp.time)
        val formatter: DateFormat = SimpleDateFormat("EEE")
        return formatter.format(date)
    }

    fun changeStatusBarColor(activity: Activity, colorID: Int) {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.statusBarColor = activity.resources.getColor(colorID)

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    fun getWeatherIcon(main: String?): Int {
        return when (main) {
            "01d" -> {
                R.drawable.clear_sky
            }
            "01n" -> {
                R.drawable.clear_sky
            }

            "02d" -> {
                R.drawable.few_clouds
            }
            "02n" -> {
                R.drawable.few_clouds
            }

            "03d" -> {
                R.drawable.scattered_clouds
            }
            "03n" -> {
                R.drawable.scattered_clouds
            }

            "04d" -> {
                R.drawable.broken_clouds
            }
            "04n" -> {
                R.drawable.broken_clouds
            }

            "09d" -> {
                R.drawable.shower_rain
            }
            "09n" -> {
                R.drawable.shower_rain
            }

            "10d" -> {
                R.drawable.extreme_rain
            }
            "10n" -> {
                R.drawable.extreme_rain
            }

            "11d" -> {
                R.drawable.thunderstorm
            }
            "11n" -> {
                R.drawable.thunderstorm
            }

            "13d" -> {
                R.drawable.snow
            }
            "13n" -> {
                R.drawable.snow
            }

            "50d" -> {
                R.drawable.fog
            }
            "50n" -> {
                R.drawable.fog
            }

            else -> {
                R.drawable.clear_sky
            }
        }
    }

    fun getWeatherIconWithNightIcons(main: String?): Int {
        return when (main) {
            "01d" -> {
                R.drawable.clear_sky
            }
            "01n" -> {
                R.drawable.clear_sky_night
            }

            "02d" -> {
                R.drawable.few_clouds
            }
            "02n" -> {
                R.drawable.few_clouds_night
            }

            "03d" -> {
                R.drawable.scattered_clouds
            }
            "03n" -> {
                R.drawable.scattered_clouds_night
            }

            "04d" -> {
                R.drawable.broken_clouds
            }
            "04n" -> {
                R.drawable.broken_clouds_night
            }

            "09d" -> {
                R.drawable.shower_rain
            }
            "09n" -> {
                R.drawable.shower_rain_night
            }

            "10d" -> {
                R.drawable.extreme_rain
            }
            "10n" -> {
                R.drawable.extreme_rain_night
            }

            "11d" -> {
                R.drawable.thunderstorm
            }
            "11n" -> {
                R.drawable.thunderstorm_night
            }

            "13d" -> {
                R.drawable.snow
            }
            "13n" -> {
                R.drawable.snow_night
            }

            "50d" -> {
                R.drawable.fog
            }
            "50n" -> {
                R.drawable.fog_night
            }

            else -> {
                R.drawable.clear_sky
            }
        }
    }

    fun openDialog(context: Context, title: String, body: String){
        val alertDialog: AlertDialog = AlertDialog.Builder(context).create()
        alertDialog.setTitle(title)
        alertDialog.setMessage(body)

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, context.getString(R.string.ok),
            DialogInterface.OnClickListener { dialog, which ->
                alertDialog.dismiss()
            })

        alertDialog.show();
    }

    fun openCustomDialog(context: Context, body: String){
        // Dialog message
        val textMessage = body
        val message = Html.fromHtml(textMessage, Html.FROM_HTML_MODE_LEGACY)

        // initialize Dialog
        var cd: CustomDialog? = null;
        cd = CustomDialog(context,
            message, context.getString(R.string.ok), null,
            View.OnClickListener { view ->
                cd!!.dismiss() // close custom dialog
            })
        cd.show()
    }
}
