package com.weatherapp.handler

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import com.github.mikephil.charting.charts.CombinedChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.CombinedData
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.weatherapp.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@SuppressLint("SimpleDateFormat")
class ChartHandler() {
    var chart: CombinedChart? = null
    var context: Context? = null
    var data: CombinedData? = null

    constructor(context: Context, chart: CombinedChart?) : this() {
        this.context = context
        this.chart = chart
        config()
        drawOrder()

        val l = chart!!.legend
        l.isWordWrapEnabled = true
        l.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        l.orientation = Legend.LegendOrientation.HORIZONTAL
        l.setDrawInside(false)
    }

    private fun config() {
        // Initialize chart
        chart!!.getDescription().setEnabled(false);
        chart!!.setBackgroundColor(Color.TRANSPARENT);
        chart!!.setDrawGridBackground(false);
        chart!!.setDrawBarShadow(false);
        chart!!.setHighlightFullBarEnabled(false);
    }

    fun drawOrder() {
        // draw bars behind lines
        chart!!.drawOrder = arrayOf(
            CombinedChart.DrawOrder.BAR,
            CombinedChart.DrawOrder.LINE
        )
    }

    fun axisRight() {
        val yAxis: YAxis = chart!!.axisRight
        yAxis.isEnabled = false;
    }

    fun axisLeft() {
        val leftAxis = chart!!.axisLeft
        leftAxis.textColor = context!!.resources.getColor(R.color.Sunrise)
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = 0f // this replaces setStartAtZero(true)
    }

    @SuppressLint("SimpleDateFormat")
    fun xAxis() {
        val xAxis: XAxis = chart!!.xAxis
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.textColor = Color.WHITE
        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f

        val initialDate = Date()
        xAxis.valueFormatter = IAxisValueFormatter() { value, axis ->
            val c: Calendar = Calendar.getInstance()
            c.setTime(initialDate)
            c.add(Calendar.HOUR, Math.round(value))
            val newDate: Date = c.getTime()
            SimpleDateFormat("hha").format(newDate)
        }
    }

    fun xAxisCustom(time: Long) {
        val xAxis: XAxis = chart!!.xAxis
        xAxis.position = XAxis.XAxisPosition.TOP
        xAxis.textColor = Color.WHITE
        xAxis.axisMinimum = 0f
        xAxis.granularity = 1f

        val initialDate = Date(time*1000)
        var add = 0f
        xAxis.valueFormatter = IAxisValueFormatter() { value, axis ->
            if(value == 0f){
                add = value
            }else{
                add = 3f * value
            }
            val c: Calendar = Calendar.getInstance()
            c.setTime(initialDate)
            c.add(Calendar.HOUR_OF_DAY, Math.round(add))
            val newDate: Date = c.getTime()
            SimpleDateFormat("hha").format(newDate)
        }
    }

    fun initChartType() {
        data = CombinedData()
    }


}