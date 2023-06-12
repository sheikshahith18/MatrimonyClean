package com.example.matrimony.db.converters

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.util.LruCache
import androidx.room.TypeConverter
import com.example.matrimony.R
import java.io.ByteArrayOutputStream

class BitmapConverter {

    private val bitmapCache = LruCache<String, Bitmap>((Runtime.getRuntime().maxMemory()/1024/8).toInt())

    @TypeConverter
    fun toBitmap(byteArray: ByteArray?): Bitmap? {
        Log.d("Image Check",byteArray.toString())
        return byteArray?.let {
            //BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
            if(byteArray.isEmpty()){
                null
            }
            else{
                val key = byteArray.contentHashCode().toString()
                val bitmap = bitmapCache.get(key) ?: BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                if (bitmap !in bitmapCache.snapshot().values) {
                    bitmapCache.put(key, bitmap)
                }

                bitmap
            }

        }
    }

    @TypeConverter
    fun fromBitmap(bitmap: Bitmap?): ByteArray?{
        val outputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.PNG,100,outputStream)
        return outputStream.toByteArray()
    }
}