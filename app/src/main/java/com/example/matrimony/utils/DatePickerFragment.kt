package com.example.matrimony.utils

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.*

class DatePickerFragment(private val isAge: Boolean?,private val dateString:String?) : DialogFragment(), DatePickerDialog.OnDateSetListener {

//    private var dateString:String?="31/12/2005"

    lateinit var datePickerListener: DatePickerListener
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)
        var datePickerDialog: DatePickerDialog? = null
        if(isAge==null){
            datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
        }
        else if (isAge) {
            calendar.add(Calendar.YEAR, -18)
            val maxDate = calendar.timeInMillis
            calendar.add(Calendar.YEAR, -25)
            val minDate = calendar.timeInMillis
            datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
            datePickerDialog.datePicker.maxDate = maxDate
            datePickerDialog.datePicker.minDate = minDate
        }else{
            calendar.add(Calendar.DATE,1)
            val minDate = calendar.timeInMillis
            datePickerDialog = DatePickerDialog(requireContext(), this, year, month, day)
            datePickerDialog.datePicker.minDate = minDate
        }

        if(dateString!=null){
            val date=getDateFromString(dateString!!)
            calendar.time=date!!
            datePickerDialog.updateDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DATE))
        }

        return datePickerDialog
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {

        val strDate = if (day < 10) "0$day" else "$day"
        val strMonth = if (month < 9) "0${month + 1}" else "${month + 1}"
        val str = "$strDate/$strMonth/$year"
        datePickerListener.date(str)
    }

}


fun interface DatePickerListener {
    fun date(date: String)
}