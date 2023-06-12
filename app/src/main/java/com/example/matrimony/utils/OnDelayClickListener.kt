package com.example.matrimony.utils

import android.os.Handler
import android.os.Looper
import android.view.View


interface OnDelayClickListener : View.OnClickListener {

    fun onDelayClick()

    override fun onClick(v: View?) {
        val handler = Handler(Looper.getMainLooper())
        v?.isClickable = false
        onDelayClick()
        handler.postDelayed({
            v?.isClickable = true
        }, 500)
    }

}