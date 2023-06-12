package com.example.matrimony.ui.loginscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.R
import com.example.matrimony.databinding.ActivityLoginViaOtpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginViaOTPActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginViaOtpBinding
    private val loginViewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login_via_otp)

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

        lifecycleScope.launch {
            if (loginViewModel.isMobileExist(binding.etMobile.text.toString())) {
                goToEnterOtpPage()
            } else {
//                runOnUiThread {
                    binding.tilMobile.isErrorEnabled = true
                    binding.tilMobile.error = "Enter registered mobile No."
                    binding.tilMobile.clearFocus()
                    binding.etMobile.clearFocus()
//                }
                val inputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(binding.etMobile.windowToken, 0)
            }
        }

    }

    private fun goToEnterOtpPage() {
        val intent = Intent(this, EnterOtpActivity::class.java)
        intent.putExtra("class_name", "LoginViaOTPActivity")
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