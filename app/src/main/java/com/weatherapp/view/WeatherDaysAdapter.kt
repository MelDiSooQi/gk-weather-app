package com.weatherapp.view

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.weatherapp.R
import com.weatherapp.databinding.RowWeatherDataBinding
import com.weatherapp.handler.Utilities
import com.weatherapp.model.forecast.WeatherData
import java.util.Locale

@SuppressLint("SetTextI18n", "DefaultLocale")
class WeatherDaysAdapter(activity: Activity, var list: List<WeatherData>?) :
    RecyclerView.Adapter<WeatherDaysAdapter.baseViewHolder>() {

    var activity: Activity? = activity
    var adapter: RecyclerView.Adapter<*>? = null

    init {
        adapter = this
    }

    // Holds the views for adding it to image and text
    class baseViewHolder(val binding: RowWeatherDataBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): baseViewHolder {
        val cellListBinding: RowWeatherDataBinding =
            RowWeatherDataBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return baseViewHolder(cellListBinding)
    }


    override fun onBindViewHolder(holder: baseViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val item: WeatherData = list!![position]


        if(position == 0){
//            if first element write today
            holder.binding.dateText.text = "Today"
        }else {
            // Change format of the timestamp to written day
            holder.binding.dateText.text = Utilities.timestamp2StringDate(item.dt!!)
        }

        if (item.weather!!.isNotEmpty()) {
            // Add the weather icon
            val weatherIcon = Utilities.getWeatherIcon(item.weather!![0].icon)
            holder.binding.weatherConditionIcon.setImageResource(weatherIcon)
        }

        // Convert temp from kelvin to Celsius
        val celsiusDegree = (Utilities.kelvinToCelsius(item.main!!.temp!!) + 0.5).toInt()

        holder.binding.weatherConditionText.text =
            item.weather!![0].description?.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            } +" "+
                    Utilities.kelvinToCelsius(item.main!!.tempMax!! + 0.5)
                        .toInt().toString() + "° /"+
                    Utilities.kelvinToCelsius(item.main!!.tempMin!! + 0.5)
                        .toInt().toString() + "°"

        holder.binding.row.setOnClickListener(View.OnClickListener { view ->
            Utilities.openCustomDialog(activity!!,
            "<b>"+activity!!.getString(R.string.location_details)+"</b><br><br>" +
                "<b>${activity!!.getString(R.string.temperature)}:</b> $celsiusDegree" +
                    "º<br>" +
                "<b>${activity!!.getString(R.string.weather)}:</b> "+item.weather!![0].main+
                "<br><b>${activity!!.getString(R.string.weather_description)}:</b> "+item.weather!![0].description
            )
        })
    }

    // Total numbers of the item in the list
    override fun getItemCount(): Int {
        return list!!.size
    }

}
