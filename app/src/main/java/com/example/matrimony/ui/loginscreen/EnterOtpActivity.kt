package com.example.matrimony.ui.loginscreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.R
import com.example.matrimony.TAG
import com.example.matrimony.databinding.ActivityEnterOtpBinding
import com.example.matrimony.ui.mainscreen.MainActivity
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit


@AndroidEntryPoint
class EnterOtpActivity : AppCompatActivity() {

    lateinit var binding: ActivityEnterOtpBinding

    private val loginViewModel by viewModels<LoginViewModel>()

    private var otp = ""
    private lateinit var firebaseAuth: FirebaseAuth

    private var verificationId: String = ""

    private lateinit var mobileNo: String


    private val mCallbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            Log.i(TAG, "onVerificationCompleted:$credential")
            val code = credential.smsCode
            if (code != null)
                verifyCode(code)
            Log.i(
                TAG,
                "${credential.smsCode} , ${credential.provider} , ${credential.signInMethod}"
            )
        }

        override fun onVerificationFailed(e: FirebaseException) {

            Toast.makeText(this@EnterOtpActivity, "Verification Failed", Toast.LENGTH_SHORT).show()
            Log.i(TAG, "onVerificationFailed ${e.message}")
        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken
        ) {

            super.onCodeSent(verificationId, token)
            Log.d(TAG, "onCodeSent:$verificationId")

            this@EnterOtpActivity.verificationId = verificationId
            Toast.makeText(this@EnterOtpActivity, "Code sent", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_enter_otp)

        firebaseAuth = FirebaseAuth.getInstance()
        otpTextChangeListener()

        mobileNo = intent.getStringExtra("mobile_no") ?: ""

        sendOtp()

        binding.btnVerifyOtp.setOnClickListener {
            verifyOtp()
        }

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun sendOtp() {

        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber("+91$mobileNo")       // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this)                 // Activity (for callback binding)
            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    private fun otpTextChangeListener() {

        val otpEditTexts = ArrayList<EditText>().apply {
            add(findViewById(R.id.et_otp_1))
            add(findViewById(R.id.et_otp_2))
            add(findViewById(R.id.et_otp_3))
            add(findViewById(R.id.et_otp_4))
            add(findViewById(R.id.et_otp_5))
            add(findViewById(R.id.et_otp_6))
        }

        val textChangedListener = object : TextWatcher {
            var tempString: String? = null
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                for (i in 0 until otpEditTexts.size) {
                    if (s == otpEditTexts[i].editableText) {
                        if (s?.isBlank() == true)
                            return
                        else if (s.toString().length >= 2 && i != otpEditTexts.size - 1) {
                            otpEditTexts[i].setText(s.toString()[0].toString())
                            tempString = s.toString().substring(1, s!!.length)
                            otpEditTexts[i + 1].setText(tempString)
                        } else if (i != otpEditTexts.size - 1)
                            otpEditTexts[i + 1].requestFocus()
                        if (i == otpEditTexts.size - 1)
                            otpEditTexts[i].setSelection(1)
                    }
                }
            }
        }

        for (i in 0 until otpEditTexts.size) {
            otpEditTexts[i].addTextChangedListener(textChangedListener)
            otpEditTexts[i].setOnKeyListener { v, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && i != 0) {
                    otpEditTexts[i - 1].requestFocus()
                    otpEditTexts[i - 1].setSelection(otpEditTexts[i - 1].length())
                }
                false
            }
        }
    }

    private fun verifyOtp() {

        if (binding.etOtp1.text.toString().isBlank()
            || binding.etOtp2.text.toString().isBlank()
            || binding.etOtp3.text.toString().isBlank()
            || binding.etOtp4.text.toString().isBlank()
            || binding.etOtp5.text.toString().isBlank()
            || binding.etOtp6.text.toString().isBlank()
        ) {
            Toast.makeText(this, "Enter OTP to Continue", Toast.LENGTH_SHORT).show()
            return
        }
        otp = binding.etOtp1.text.toString() +
                binding.etOtp2.text.toString() +
                binding.etOtp3.text.toString() +
                binding.etOtp4.text.toString() +
                binding.etOtp5.text.toString() +
                binding.etOtp6.text.toString()

        Log.i(
            TAG,
            "${intent.getStringExtra("class_name")} and ${ForgotPasswordActivity::class.simpleName}"
        )

        verifyCode(otp)
    }

    private fun verifyCode(code: String) {
        val credentials: PhoneAuthCredential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credentials)
    }

    private fun signInWithCredential(credentials: PhoneAuthCredential) {
        val fireBaseAuth = FirebaseAuth.getInstance()
        fireBaseAuth.signInWithCredential(credentials).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "OTP validated successfully", Toast.LENGTH_SHORT).show()
                if (intent.getStringExtra("class_name") == ForgotPasswordActivity::class.simpleName) {
                    startActivity(Intent(this, ResetPasswordActivity::class.java))
                    finish()
                } else if (intent.getStringExtra("class_name") == LoginViaOTPActivity::class.simpleName) {

                    lifecycleScope.launch {
                        val sharedPref =
                            getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)!!
                        val editor = sharedPref.edit()
                        editor.putInt(
                            CURRENT_USER_ID,
                            loginViewModel.getUserUsingEmailOrMobile(mobileNo)
                        )
                        Log.i(
                            TAG,
                            "EnterOtp fetchedUser ${sharedPref.getInt(CURRENT_USER_ID, -11)}"
                        )
                        editor.apply()
                        val intent=Intent(this@EnterOtpActivity, MainActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        firebaseAuth.signOut()
                        finish()
                    }
                }
            } else
                Toast.makeText(this, "Invalid OTP", Toast.LENGTH_SHORT).show()
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