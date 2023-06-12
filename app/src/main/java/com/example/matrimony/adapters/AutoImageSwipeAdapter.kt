package com.example.matrimony.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.matrimony.ui.mainscreen.homescreen.ImageViewFragment

class AutoImageSwipeAdapter(fm: FragmentManager, private val images: List<Int>,private val coupleNames:List<String>) :
    FragmentStatePagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        val image = images[position % images.size]
        val coupleName = coupleNames[position % coupleNames.size]
        return ImageViewFragment.newInstance(image,coupleName)
    }

    override fun getCount(): Int {
        return Int.MAX_VALUE
    }

}