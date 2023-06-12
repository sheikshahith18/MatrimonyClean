package com.example.matrimony.ui.loginscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.matrimony.R
import com.example.matrimony.databinding.ActivityForgotPasswordBinding
import com.example.matrimony.utils.MY_SHARED_PREFERENCES

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityForgotPasswordBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)

        binding.btnSendOtp.setOnClickListener {
            sendOtp()
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.etMobile.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus)
                binding.tilMobile.isErrorEnabled = false
        }
    }

    private fun sendOtp() {

        if (binding.etMobile.text?.isBlank() == true) {
            return
        }

        GlobalScope.launch {
            if (loginViewModel.isMobileExist(binding.etMobile.text.toString())) {
                goToEnterOtpPage()
            } else {
                runOnUiThread {
                    binding.tilMobile.isErrorEnabled = true
                    binding.tilMobile.error = "Enter registered mobile No."
                    binding.tilMobile.clearFocus()
                    binding.etMobile.clearFocus()
                }
//                val inputMethodManager =
//                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                inputMethodManager.hideSoftInputFromWindow(binding.etMobile.windowToken, 0)
            }
        }

    }

    private fun goToEnterOtpPage() {

        val intent = Intent(this, EnterOtpActivity::class.java)
        intent.putExtra("class_name", "ForgotPasswordActivity")
        intent.putExtra("mobile_no","${binding.etMobile.text.toString()}")
        startActivity(intent)
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}