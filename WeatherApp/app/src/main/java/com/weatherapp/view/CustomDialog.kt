package com.weatherapp.view

import android.app.Activity
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.Button
import com.weatherapp.R


class CustomDialog
    (private var activity: Activity)
    : Dialog(activity), View.OnClickListener {

    var dialog: Dialog? = this
    var yes: Button? = null
    var no: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        yes = findViewById<View>(R.id.btn_yes) as Button
        no = findViewById<View>(R.id.btn_no) as Button
        yes!!.setOnClickListener(this)
        no!!.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.btn_yes -> activity.finish()
            R.id.btn_no -> dismiss()
            else -> {}
        }
        dismiss()
    }
}