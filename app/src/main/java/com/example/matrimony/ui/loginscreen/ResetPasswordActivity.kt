package com.example.matrimony.ui.loginscreen

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ActivityResetPasswordBinding
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import com.example.matrimony.utils.Validator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : AppCompatActivity() {

    lateinit var binding: ActivityResetPasswordBinding
    private val loginViewModel by viewModels<LoginViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_reset_password)

        registerFocusListeners()

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.btnResetPass.setOnClickListener {
            resetPassword()
        }
    }

    private fun registerFocusListeners() {
        //password
        binding.etPassword.setOnFocusChangeListener { v, hasFocus ->
            if (binding.etPassword.text?.isBlank() == true)
                return@setOnFocusChangeListener
            if (!hasFocus) {
                if (!Validator.strongPasswordValidator(binding.etPassword.text.toString())) {
                    binding.tilPassword.isErrorEnabled = true
                    binding.tilPassword.error = "Enter Strong Password"
                } else {
                    binding.tilPassword.isErrorEnabled = false
                }
            } else {
                binding.tilPassword.isErrorEnabled = false
            }
        }

        //confirm password
        binding.etConfirmPass.setOnFocusChangeListener { v, hasFocus ->
            if (binding.etConfirmPass.text?.isBlank() == true)
                return@setOnFocusChangeListener
            if (!hasFocus) {
                if (!Validator.confirmPasswordValidator(
                        binding.etPassword.text.toString(),
                        binding.etConfirmPass.text.toString()
                    )
                ) {
                    binding.tilConfirmPass.isErrorEnabled = true
                    binding.tilConfirmPass.error = "Passwords didn't match"
                } else {
                    binding.tilConfirmPass.isErrorEnabled = false
                }
            } else {
                binding.tilConfirmPass.isErrorEnabled = false
            }
        }
    }

    private fun resetPassword() {
        if (binding.etPassword.text?.isBlank() == true || binding.etConfirmPass.text?.isBlank() == true)
            return

        if (binding.tilPassword.isErrorEnabled || binding.tilConfirmPass.isErrorEnabled) {
            return
        }


        val isPasswordStrong = Validator.strongPasswordValidator(binding.etPassword.text.toString())
        val isPasswordsMatched = Validator.confirmPasswordValidator(
            binding.etPassword.text.toString(),
            binding.etConfirmPass.text.toString()
        )

        if (!isPasswordStrong) {
            Log.i(TAG,"Pass weak")
            binding.tilPassword.isErrorEnabled = true
            binding.tilPassword.error = "Enter Strong Password"
            binding.tilPassword.clearFocus()
        }
        if (!isPasswordsMatched) {
            Log.i(TAG,"Pass mismatch")
            binding.tilConfirmPass.isErrorEnabled = true
            binding.tilConfirmPass.error = "Passwords didn't match"
            binding.tilConfirmPass.clearFocus()
        }

        if (isPasswordsMatched && isPasswordStrong) {
            if(intent.getStringExtra("sender")=="EnterPasswordActivity")
                Toast.makeText(this, "Password Changed Successfully", Toast.LENGTH_LONG).show()
            else
                Toast.makeText(this, "Password Reset Successfully, Login to continue", Toast.LENGTH_LONG).show()
            val sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
            val userId = sharedPref.getInt(CURRENT_USER_ID, -1)
            if (userId == -1)
                return
            loginViewModel.updatePassword(userId, binding.etPassword.text.toString())
            finish()
        }

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