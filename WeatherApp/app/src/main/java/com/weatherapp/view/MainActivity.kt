package com.weatherapp.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.weatherapp.R
import com.weatherapp.databinding.ActivityMainBinding
import com.weatherapp.databinding.ActivitySplashBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}