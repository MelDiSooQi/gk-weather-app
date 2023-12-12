package com.weatherapp.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.weatherapp.R
import com.weatherapp.databinding.ActivitySplashBinding

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    private val REQUEST_CODE_LOCATION = 24

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Splash Screen
        val logoTimer: Thread = object : Thread() {
            override fun run() {
                try {
                    // sleep for 1 second
                    sleep(1000)
                    this@SplashActivity.runOnUiThread(Runnable {
                        //You can't custom layout for the permission dialog as it shows in the google source below.
                        //https://developer.android.com/training/permissions/requesting.html
                        // You can custom only normal dialog not a permission dialog
                        openCustomDialog() //open Custom Dialog to ask for location permission
                    })

                } catch (ignored: InterruptedException) {
                } finally {
                }
            }
        }
        // start timer for splash screen
        logoTimer.start()
    }

    private fun openCustomDialog() {
        // Dialog message
        val textMessage = "${getString(R.string.allow)} <b>${getString(R.string.app_name)}</b> " +
                getString(R.string.permission_message_1) +
                "<br>" +
                "<br>" + getString(R.string.permission_message_2)
        val message = Html.fromHtml(textMessage, Html.FROM_HTML_MODE_LEGACY)

        // initialize Dialog
        var cd: CustomDialog? = null;
        cd = CustomDialog(this@SplashActivity,
            message, getString(R.string.allow), getString(R.string.deny),
            View.OnClickListener { view ->
                when (view.id) {
                    R.id.btn_yes -> {
                        accessPermission(cd) // ask for location permission
                        cd!!.dismiss() // close custom dialog
                    }
                    R.id.btn_no -> {
                        // Open Error Message activity
                        openErrorMessageScreen()
                        cd!!.dismiss() // close custom dialog
                    }
                    else -> {
                        cd!!.dismiss() // close custom dialog
                        this@SplashActivity.finish() // turn off the current screen
                    }
                }
            })
        accessPermission(cd) // ask for location permission
    }

    private fun accessPermission(customDialog: CustomDialog?) {
        val accessCoarseLocation = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        val listRequestPermission: MutableList<String> = java.util.ArrayList()

        // Request location permission
        if (accessCoarseLocation != PackageManager.PERMISSION_GRANTED) {
            listRequestPermission.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if (listRequestPermission.isNotEmpty()) {
            if(!customDialog!!.isShowing){
                // open Custom Dialog
                customDialog!!.show()
            }else{
                // open permission dialog
                val strRequestPermission = listRequestPermission.toTypedArray()
                requestPermissions(strRequestPermission, REQUEST_CODE_LOCATION)
            }
        }else{
            // if all PERMISSION GRANTED
            // Open Main activity
            openMainScreen()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (REQUEST_CODE_LOCATION == requestCode) {
            if (grantResults[0] == 0) {  // access grant allowed
                this@SplashActivity.runOnUiThread(Runnable {
                    // Open Main activity
                    openMainScreen()
                })
            } else if (grantResults[0] == -1) { // access grant not allowed
                this@SplashActivity.runOnUiThread(Runnable {
                    // Open Error Message activity
                    openErrorMessageScreen()
                })
            }
        }
    }

    private fun openMainScreen() {
        // Open Main activity
        val intent = Intent(this@SplashActivity,
            MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun openErrorMessageScreen() {
        // Open Error Message activity
        val intent = Intent(this@SplashActivity,
            ErrorMessagesActivity::class.java)
        startActivity(intent)
        finish()
    }
}