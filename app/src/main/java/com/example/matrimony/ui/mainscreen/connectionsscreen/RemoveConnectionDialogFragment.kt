package com.example.matrimony.ui.mainscreen.connectionsscreen

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.matrimony.R
import com.example.matrimony.ui.mainscreen.searchscreen.SearchPageFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RemoveConnectionDialogFragment :
    DialogFragment() {


    lateinit var removeConnectionListener: RemoveConnectionListener


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_remove_connection_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(1000, WindowManager.LayoutParams.WRAP_CONTENT)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tv_remove_connection).setOnClickListener {
            removeConnectionListener.onButtonClick()
            dismiss()
        }

        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
//            removeConnectionListener.onButtonClick("CANCEL")
            dismiss()
        }
    }


}


fun interface RemoveConnectionListener {
    fun onButtonClick()
}
