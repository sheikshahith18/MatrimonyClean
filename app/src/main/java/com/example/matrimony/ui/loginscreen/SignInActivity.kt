package com.example.matrimony.ui.loginscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ActivitySignInBinding
import com.example.matrimony.ui.mainscreen.MainActivity
import com.example.matrimony.ui.registerscreen.SignUpActivity
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private val loginViewModel by viewModels<LoginViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in)


        binding.tvForgotPassword.setOnClickListener {
            forgotPassword()
        }

        binding.tvLoginViaOtp.setOnClickListener {
            loginViaOTP()
        }

        binding.tvCreateNewAcc.setOnClickListener {
            createNewAccount()
        }

        binding.btnSignIn.setOnClickListener {
            validateSignIn()
        }

        registerFocusListeners()

    }

    private fun registerFocusListeners() {
        binding.etEmailOrMobile.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tilEmailOrMobile.isErrorEnabled = false
            }
        }
        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.tilPassword.isErrorEnabled = false
            }
        }
    }

    private fun forgotPassword() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
    }

    private fun loginViaOTP() {
        val intent = Intent(this, LoginViaOTPActivity::class.java)
        startActivity(intent)

    }

    private fun createNewAccount() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun validateSignIn() {

        if (binding.etEmailOrMobile.text?.isBlank() == true || binding.etPassword.text?.isBlank() == true)
            return

        lifecycleScope.launch {
            if (loginViewModel.isCredentialsMatched(
                    binding.etEmailOrMobile.text.toString(),
                    binding.etPassword.text.toString()
                )
            ) {
                Log.i(TAG, "details matched")
                val sharedPref =
                    getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)!!
                val editor = sharedPref.edit()
                editor.putInt(
                    CURRENT_USER_ID,
                    loginViewModel.getUserUsingEmailOrMobile(binding.etEmailOrMobile.text.toString())
                )
                editor.apply()

                val intent = Intent(this@SignInActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {

                if (loginViewModel.isEmailExist(binding.etEmailOrMobile.text.toString()) || loginViewModel.isMobileExist(
                        binding.etEmailOrMobile.text.toString()
                    )
                ) {
                    binding.tilPassword.isErrorEnabled = true
                    binding.tilPassword.error = "Incorrect Password"
                    binding.tilPassword.clearFocus()
                } else {
                    binding.tilEmailOrMobile.isErrorEnabled = true
                    binding.tilEmailOrMobile.error = "Invalid Email/Mobile No."
                    binding.tilEmailOrMobile.clearFocus()
                    binding.tilPassword.clearFocus()
                    binding.tilPassword.isErrorEnabled = false
//                    binding.etPassword.setText("")
                }
//                Toast.makeText(this@SignInActivity, "Invalid Credentials", Toast.LENGTH_SHORT)
//                    .show()
            }
        }

    }

}