package com.example.matrimony.ui.mainscreen.homescreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.matrimony.R

class ImageViewFragment : Fragment() {

    private var image: Int = 0
    private var text: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            image = it.getInt(ARG_IMAGE)
            text = it.getString(ARG_TEXT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_success_stories, container, false)
        view.findViewById<ImageView>(R.id.imageView).setImageResource(image)
        view.findViewById<TextView>(R.id.tv_couple_name).text = text
        return view
    }

    companion object {
        private const val ARG_IMAGE = "image"
        private const val ARG_TEXT = "couple_name"

        fun newInstance(image: Int, coupleName: String): ImageViewFragment {
            val fragment = ImageViewFragment()
            val args = Bundle()
            args.putInt(ARG_IMAGE, image)
            args.putString(ARG_TEXT, coupleName)
            fragment.arguments = args
            return fragment
        }
    }
}
