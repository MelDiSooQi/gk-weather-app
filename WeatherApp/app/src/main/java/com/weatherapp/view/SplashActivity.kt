package com.weatherapp.view

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.weatherapp.databinding.ActivitySplashBinding

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private var fusedLocationClient: FusedLocationProviderClient? = null
    private val REQUEST_CODE_LOCATION = 24
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val cd = CustomDialog(this@SplashActivity)
        cd.show()

        //Splash Screen
        val logoTimer: Thread = object : Thread() {
            override fun run() {
                try {
                    // sleep for 2 seconds
                    sleep(2000)
//                    accessPermission() // ask for location permission



                } catch (ignored: InterruptedException) {
                } finally {
                }
            }
        }

        logoTimer.start()
    }

    private fun accessPermission() {
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (REQUEST_CODE_LOCATION == requestCode) {
            if (grantResults[0] == 0) {  // access grant allowed
//                getCurrentLocation()
                openMainScreen()
            } else if (grantResults[0] == -1) { // access grant not allowed
//                binding.status.text = getString(R.string.location_permission)
//                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun openMainScreen() {
        val intent = Intent(this@SplashActivity,
            MainActivity::class.java)
        startActivity(intent)
        finish()

    }
}