package com.example.matrimony.adapters

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.example.matrimony.R

open class CustomArrayAdapter(context: Context, resource: Int, objects: List<String>) :
    ArrayAdapter<Any>(context, resource, objects) {

    private var selectedPositions= mutableSetOf<Int>()

    private var selectedPosition = -1

    fun setSelectedPosition(position: Int) {
        selectedPositions.add(position)
        selectedPosition = position
        notifyDataSetChanged()
    }

    fun removeSelectedPosition(position: Int){
        selectedPositions.remove(position)
        notifyDataSetChanged()
    }

    fun removeAllSelections(){
        selectedPositions.clear()
        notifyDataSetChanged()
    }

    fun clearSelection() {
        selectedPosition = -1
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = super.getView(position, convertView, parent)
        if (selectedPositions.contains(position)) {
            (view as TextView).setTextColor(context.resources.getColor(android.R.color.holo_blue_dark,null))
//            view.setBackgroundColor(Color.GREEN)
        } else {
//            (view as TextView).setTextColor(Color.TRANSPARENT)
            (view as TextView).setTextColor(context.resources.getColor(R.color.text_color,null))
        }
        return view
    }
}
