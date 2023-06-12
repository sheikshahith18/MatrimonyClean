package com.example.matrimony.ui.mainscreen.homescreen.settingsscreen

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.matrimony.R
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DeleteAccountDialogFragment : DialogFragment() {


    companion object {
        fun interface DeleteAccountListener {
            fun deleteAccount()
        }
    }

    private lateinit var deleteAccountListener: DeleteAccountListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        deleteAccountListener=context as EnterPasswordActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return inflater.inflate(R.layout.fragment_delete_account_dialog, container, false)
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            900,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.tv_delete_account).setOnClickListener {
            deleteAccountListener.deleteAccount()
            dismiss()
        }

        view.findViewById<TextView>(R.id.tv_cancel).setOnClickListener {
//            removeConnectionListener.onButtonClick("CANCEL")
            dismiss()
        }
    }

}