package com.weatherapp.view

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Spanned
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.weatherapp.R

class CustomDialog(private val context: Context, private val message: Spanned,
                   private val yesBtn: String, private val noBtn: String?,
                   private val onClickListener: View.OnClickListener)
    : Dialog(context)  {

    var dialog: Dialog? = this
    var txtMessage: TextView? = null
    var yes: Button? = null
    var no: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.custom_dialog)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        txtMessage = findViewById<View>(R.id.txt_dia) as TextView
        txtMessage!!.text = message

        yes = findViewById<View>(R.id.btn_yes) as Button
        no = findViewById<View>(R.id.btn_no) as Button

        yes!!.text = yesBtn
        if(noBtn != null) {
            no!!.text = noBtn
            no!!.visibility = View.VISIBLE
        }else{
            no!!.visibility = View.GONE
        }

        yes!!.setOnClickListener(onClickListener)
        no!!.setOnClickListener(onClickListener)
    }

}