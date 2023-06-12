package com.example.matrimony.ui.mainscreen.homescreen.profilescreen.albumscreen

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.cardview.widget.CardView
import androidx.fragment.app.DialogFragment
import com.example.matrimony.R
import com.example.matrimony.TAG

class OpenCameraDialogFragment :DialogFragment(){

    companion object {
        interface ButtonClickListener {
            fun onButtonClick(clickedItem: String)
        }
    }

    lateinit var buttonClickListener: ButtonClickListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.i(TAG,"OpenCamera onAttach")

            buttonClickListener =  context as AlbumActivity1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.select_image_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(1000, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<CardView>(R.id.open_camera).setOnClickListener {
            buttonClickListener.onButtonClick("camera")
            dismiss()
        }

        view.findViewById<CardView>(R.id.open_gallery).setOnClickListener {
            buttonClickListener.onButtonClick("gallery")
            dismiss()
        }
    }


}