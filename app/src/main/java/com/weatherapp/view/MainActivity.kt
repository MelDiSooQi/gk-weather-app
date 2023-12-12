package com.weatherapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.Typeface
import android.location.Location
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.weatherapp.R
import com.weatherapp.applicationmanger.Constants
import com.weatherapp.backend.GetCurrentDayWeather
import com.weatherapp.backend.GetForecast
import com.weatherapp.backend.configuration.BackendException
import com.weatherapp.backend.configuration.ServerCallback
import com.weatherapp.databinding.ActivityMainBinding
import com.weatherapp.handler.ChartHandler
import com.weatherapp.handler.Utilities
import com.weatherapp.model.forecast.Forecast
import com.weatherapp.model.forecast.WeatherData
import com.weatherapp.model.weather.WeatherDay
import retrofit2.Response


@SuppressLint("MissingPermission", "SetTextI18n")
class MainActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMainBinding

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val REQUEST_CODE_LOCATION = 24
    private var currentLocation: Location? = null

    private var hourlyList: ArrayList<Entry>? = null
    private var rainLevelList: ArrayList<BarEntry>? = null
    private var humidityList: ArrayList<BarEntry>? = null
    private var list: ArrayList<WeatherData>? = null
    private var adapter: WeatherDaysAdapter? = null

    private var chartHandler: ChartHandler? = null
    private var currentLocationWeather: WeatherDay? = null
    private var googleMap: GoogleMap? = null

    protected var tfLight: Typeface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hourlyList = ArrayList<Entry>()
        rainLevelList = ArrayList<BarEntry>()
        humidityList = ArrayList<BarEntry>()

        /* initialize recycler view and adaptor */
        initializeList()

        // get current location latitude and longitude
        getCurrentLocation()
        //init swipe refresh
        /* Using swipeRefresh to refresh all the data and update it
         (Current Day Weather and 5-day forecast with details) */
        binding.swipeRefreshLayout.setOnRefreshListener {
            // get current location latitude and longitude
            getCurrentLocation()
        }

        initializeChart()

        // Get a handle to the fragment and register the callback.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun initializeChart() {
        chartHandler = ChartHandler(this, binding.cardForecastHourly.hourlyForecastChart)
        chartHandler!!.axisRight()
        chartHandler!!.axisLeft()
        chartHandler!!.initChartType()
    }

    private fun updateChart(
        lineEntries: ArrayList<Entry>?,
        barEntries1: ArrayList<BarEntry>?, barEntries2: ArrayList<BarEntry>?
    ) {
        val data = chartHandler!!.data

        data!!.setData(generateLineData(lineEntries!!))
        data!!.setData(generateBarData(barEntries1!!, barEntries2!!))

        data!!.setValueTypeface(tfLight)

        chartHandler!!.chart!!.xAxis.axisMaximum = data.xMax + 0.25f

        chartHandler!!.chart!!.data = data
        chartHandler!!.chart!!.invalidate() //Refresh

        // add animation
        chartHandler!!.chart!!.animateY(1500);
    }

    private fun generateLineData(entries: ArrayList<Entry>): LineData? {
        val d = LineData()

        val set = LineDataSet(entries, "Temperature")
        set.color = Color.rgb(225, 107, 20)
        set.lineWidth = 2.5f
        set.setCircleColor(Color.rgb(225, 107, 20))
        set.circleRadius = 0f
        set.fillColor = Color.rgb(225, 107, 20)
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 10f
        set.valueTextColor = Color.rgb(225, 107, 20)
        set.axisDependency = YAxis.AxisDependency.LEFT
        d.addDataSet(set)
        return d
    }

    private fun generateBarData(entries1: ArrayList<BarEntry>, entries2: ArrayList<BarEntry>): BarData {
        val set1 = BarDataSet(entries1, "Humidity")
        set1.color = Color.rgb(39, 36, 37)
        set1.valueTextColor = Color.rgb(39, 36, 37)
        set1.valueTextSize = 10f
        set1.axisDependency = YAxis.AxisDependency.LEFT
        val set2 = BarDataSet(entries2, "Rain")
        set2.setColors(Color.rgb(61, 165, 255))
        set2.valueTextColor = Color.rgb(61, 165, 255)
        set2.valueTextSize = 10f
        set2.axisDependency = YAxis.AxisDependency.LEFT
        val groupSpace = 0.06f
        val barSpace = 0.02f // x2 dataset
        val barWidth = 0.45f // x2 dataset
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"
        val d = BarData(set1, set2)
        d.barWidth = barWidth

        // make this BarData object grouped
        d.groupBars(0f, groupSpace, barSpace) // start at x = 0

        return d
    }

    private fun initializeList() {
        /* initialize recycler view and adaptor */
        binding.cardForecast5day.recyclerView5Days.layoutManager =
            LinearLayoutManager(this@MainActivity)
        list = ArrayList<WeatherData>()

        adapter = WeatherDaysAdapter(this@MainActivity, list)
        binding.cardForecast5day.recyclerView5Days.adapter = adapter

        initializeListView()
    }

    private fun initializeListView() {
        /* initialize List View layout views */
        if (list != null) {
            if (list!!.size != 0) {
                binding.cardForecast5day.weatherStatusText.visibility = View.GONE
                binding.cardForecast5day.recyclerView5Days.visibility = View.VISIBLE
            } else {
                binding.cardForecast5day.weatherStatusText.visibility = View.VISIBLE
                binding.cardForecast5day.recyclerView5Days.visibility = View.GONE
            }
        } else {
            binding.cardForecast5day.weatherStatusText.visibility = View.VISIBLE
            binding.cardForecast5day.recyclerView5Days.visibility = View.GONE
        }
    }

    private fun getCurrentLocation() {
        accessPermission() // ask for location permission

        // init location
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // request last location
        fusedLocationClient!!.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                currentLocation = location
                /* call the api to get Current Day Weather and 5-day forecast with details */
                getData()
            } else {
                // no location available
                binding.cardForecast5day.weatherStatusText.text = getString(R.string.location_settings)
                binding.weatherConditionText.text = getString(R.string.location_settings)
                Utilities.openCustomDialog(this@MainActivity,
                    "<b>"+getString(R.string.error_message)+"</b><br><br>" +
                            getString(R.string.check_your_location)
                )
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.check_your_location),
                    Toast.LENGTH_LONG
                ).show()
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun getData() {
        if (currentLocation != null) {
            /* call the api to get Current Day Weather with details */
            getCurrentDayWeather(this@MainActivity)
            /* call the api to get 5-day forecast with details */
            getForecast(this@MainActivity)
        }
    }

    private fun getCurrentDayWeather(activity: Activity) {
        try {
            /* swipeRefresh make it start loading */
            binding.swipeRefreshLayout.isRefreshing = true

            // request data and send URL, APP ID, Latitude and Longitude
            GetCurrentDayWeather<WeatherDay>(activity, Constants.BASE_URL, true,
                Constants.OPEN_WEATHER_API_KEY,
                currentLocation!!.latitude,
                currentLocation!!.longitude,
                object : ServerCallback<WeatherDay> {
                    @SuppressLint("NewApi")
                    override fun onResponse(statusCode: Int, response: Response<*>) {
                        // Cast response to WeatherDay
                        val weatherData = response.body() as WeatherDay

                        currentLocationWeather = weatherData
                        addMarker()

                        // Display location name
                        binding.location.text = weatherData.name

                        // Convert temp from kelvin to Celsius
                        val weatherDegree =
                            Utilities.kelvinToCelsius(weatherData.main!!.temp!!+0.5).toInt()

                        // Display temp in Celsius
                        binding.ambientTemperatureText.text = "$weatherDegree°"

                        // Display weather status and tempMax and tempMin
                        binding.weatherConditionText.text =
                            weatherData.weather!![0].description+" "+
                            Utilities.kelvinToCelsius(weatherData.main!!.tempMax!!+0.5)
                            .toInt().toString() + "° /"+
                            Utilities.kelvinToCelsius(weatherData.main!!.tempMin!!+0.5)
                            .toInt().toString() + "°"

                        // Check if there are details or not
                        if (weatherData.weather!!.isNotEmpty()) {
                            // Change status bar for the app to fit the color of the theme
                            val weatherConditionIcon = Utilities.getWeatherIcon(weatherData.weather!![0].icon)
                            binding.weatherConditionIcon.setImageResource(weatherConditionIcon)


                            // Change status bar for the app to fit the color of the theme
//                            val weatherStatusBarColor = Utilities.getWeatherStatusBarColor(weatherData.weather!![0].main)
//                            Utilities.changeStatusBarColor(this@MainActivity, weatherStatusBarColor)

                            // Change background color for the app to fit the color of the theme
//                            val weatherBackgroundColor = Utilities.getWeatherBackgroundColor(weatherData.weather!![0].main)
//                            binding.mainView.setBackgroundResource(weatherBackgroundColor)

                            // Change background image for the app to fit the weather theme
//                            val weatherBackground = Utilities.getWeatherBackground(weatherData.weather!![0].main)
//                            binding.imageStatus.setImageResource(weatherBackground)
                        }

                        /* swipeRefresh make it stop loading */
                        binding.swipeRefreshLayout.isRefreshing = false
                    }

                    override fun onFailure(statusCode: Int, throwable: Throwable?) {
                        // Handle Backend Exception
                        Utilities.openCustomDialog(activity!!,
                        "<b>"+activity!!.getString(R.string.error_message)+"</b><br><br>" +
                            throwable?.message + " - " + statusCode
                        )

                        Toast.makeText(
                            this@MainActivity,
                            throwable?.message + " - " + statusCode,
                            Toast.LENGTH_SHORT
                        ).show()
                        /* swipeRefresh make it stop loading */
                        binding.swipeRefreshLayout.isRefreshing = false
                    }

                })
        } catch (e: BackendException) {
            // Handle Backend Exception
            Utilities.openCustomDialog(activity!!,
                "<b>"+activity!!.getString(R.string.error_message)+"</b><br><br>" +
                        e.message
            )

            // Change the status
            binding.weatherConditionText.text = getString(R.string.internet_connection)
            binding.cardForecast5day.weatherStatusText.text = getString(R.string.location_settings)
            // Notify the status
            Toast.makeText(this, e.message,
                Toast.LENGTH_SHORT).show()
            /* swipeRefresh make it stop loading */
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun getForecast(activity: Activity) {
        try {
            /* swipeRefresh make it start loading */
            binding.swipeRefreshLayout.isRefreshing = true

            GetForecast<Forecast>(activity, Constants.BASE_URL, true,
                Constants.OPEN_WEATHER_API_KEY,
                currentLocation!!.latitude,
                currentLocation!!.longitude,
                object : ServerCallback<Forecast> {
                    override fun onResponse(statusCode: Int, response: Response<*>) {
                        val forecast = response.body() as Forecast
                        val weatherData: List<WeatherData>? = forecast.weatherData

                        // Clear the old values and make sure the list is
                        // empty before we add new values
                        hourlyList!!.clear()
                        rainLevelList!!.clear()
                        humidityList!!.clear()
                        list!!.clear()

                        // init first day
                        var day = -1
                        // Filter request from 3 every hours to only one time in a day
                        // Add only one value from a day, by selected only first value in the day
                        for (i in 0 until weatherData!!.size) {
                            val weatherDataRecord: WeatherData = weatherData[i]
                            var temp = Utilities.kelvinToCelsius(weatherDataRecord.main!!.temp!!).toFloat()
                            val value = Entry(i + 0.5f, temp)
                            hourlyList!!.add(value)
                            if(weatherDataRecord.rain == null || weatherDataRecord.rain!!.h3 == null){
                                rainLevelList!!.add(BarEntry(i + 0.5f, 0f))
                            }else{
                                rainLevelList!!.add(BarEntry(i + 0.5f, weatherDataRecord.rain!!.h3!!))
                            }

                            humidityList!!.add(BarEntry(0f, weatherDataRecord.main!!.humidity!!.toFloat()))

                            if (day != Utilities.timestamp2Day(weatherDataRecord.dt!!)) {
                                day = Utilities.timestamp2Day(weatherDataRecord.dt!!)
                                list!!.add(weatherDataRecord)
                            }
                        }

                        chartHandler!!.xAxisCustom(weatherData[0].dt!!)
                        updateChart(hourlyList, humidityList, rainLevelList)

                        if (list!!.size >= 6) { // to remove today if it's available and get only next 5 days
//                            list!!.removeAt(0) // remove current day (today) weather
                            list!!.removeAt(list!!.size-1) // remove last day weather
                        }

                        // Update the adapter.
                        initializeListView()
                        adapter!!.notifyDataSetChanged() // notify changes to adapter


                        /* swipeRefresh make it stop loading */
                        binding.swipeRefreshLayout.isRefreshing = false
                    }

                    override fun onFailure(statusCode: Int, throwable: Throwable?) {
                        // Handle Backend Exception
                        Utilities.openCustomDialog(activity!!,
                            "<b>"+activity!!.getString(R.string.error_message)+"</b><br><br>" +
                                    throwable?.message + " - " + statusCode
                        )

                        Toast.makeText(
                            this@MainActivity,
                            throwable?.message + "" + statusCode,
                            Toast.LENGTH_SHORT
                        ).show()
                        /* swipeRefresh make it stop loading */
                        binding.swipeRefreshLayout.isRefreshing = false
                    }

                })
        } catch (e: BackendException) {
            // Handle Backend Exception
            // Change the status
            binding.weatherConditionText.text = getString(R.string.internet_connection)
            /* swipeRefresh on make it stop loading */
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun accessPermission() {
        // only request permission for android M and above
        val accessCoarseLocation = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val listRequestPermission: MutableList<String> = java.util.ArrayList()

        // Request location permission
        if (accessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (listRequestPermission.isNotEmpty()) {
            val strRequestPermission = listRequestPermission.toTypedArray()
            requestPermissions(strRequestPermission, REQUEST_CODE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (REQUEST_CODE_LOCATION == requestCode) {
            if (grantResults[0] == 0) {  // access grant allowed
                getCurrentLocation()
            } else if (grantResults[0] == -1) { // access grant not allowed
                binding.cardForecast5day.weatherStatusText.text = getString(R.string.location_permission)
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.isTrafficEnabled = true

        addMarker()
    }

    private fun addMarker() {
        if(googleMap != null) {
            googleMap!!.clear()
            if (currentLocationWeather != null) {
                // Convert temp from kelvin to Celsius
                val weatherDegree =
                    Utilities.kelvinToCelsius(currentLocationWeather!!.main!!.temp!! + 0.5).toInt()

                var currentLocation =
                    LatLng(currentLocation!!.latitude!!, currentLocation!!.longitude!!)
                googleMap!!.addMarker(
                    MarkerOptions()
                        .position(currentLocation)
                        .title(
                            currentLocationWeather!!.name + " - " + weatherDegree
                                    + " - " + currentLocationWeather!!.weather!![0].description
                        )
                )
                //Move the camera to currentLocation and zoom in!
                googleMap!!.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(
                        currentLocation, 12.0f
                    )
                )
            }
        }
    }

}