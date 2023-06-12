package com.example.matrimony

import android.app.Application
import android.database.CursorWindow
import dagger.hilt.android.HiltAndroidApp
import java.lang.reflect.Field


@HiltAndroidApp
class MyApp:Application(){
    override fun onCreate() {
        super.onCreate()
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 100 * 1024 * 1024) //the 100MB is the new size
        } catch (_: Exception) {

        }
    }
}