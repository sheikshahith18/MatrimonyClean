package com.example.matrimony.utils

import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class TimePickerFragment(private val timeString: String?) : DialogFragment(),
    TimePickerDialog.OnTimeSetListener {

    lateinit var timePickerListener: TimePickerListener

//    private var timeString: String? = "20:20"


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val hour: Int
        val minute: Int
        if (timeString != null) {

            val parts = timeString.split(":")

            if (parts.size != 2) {
                throw java.lang.Exception("Invalid Time Format")
            }
            hour = parts[0].toInt()
            minute = parts[1].toInt()
        } else {

            val c = Calendar.getInstance()
            hour = c.get(Calendar.HOUR_OF_DAY)
            minute = c.get(Calendar.MINUTE)
        }

        return TimePickerDialog(activity, this, hour, minute, false)
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val str = "$hourOfDay : $minute"
        timePickerListener.time(str)
    }
}

fun interface TimePickerListener {
    fun time(data: String)
}