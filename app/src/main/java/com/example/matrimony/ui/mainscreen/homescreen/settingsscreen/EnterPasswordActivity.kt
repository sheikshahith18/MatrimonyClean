package com.example.matrimony.ui.mainscreen.homescreen.settingsscreen

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.matrimony.R
import com.example.matrimony.ui.loginscreen.ResetPasswordActivity
import com.example.matrimony.ui.loginscreen.SignInActivity
import com.example.matrimony.ui.mainscreen.MainActivity
import com.example.matrimony.ui.mainscreen.UserProfileViewModel
import com.example.matrimony.utils.CURRENT_USER_GENDER
import com.example.matrimony.utils.CURRENT_USER_ID
import com.example.matrimony.utils.MY_SHARED_PREFERENCES
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EnterPasswordActivity : AppCompatActivity(),
    DeleteAccountDialogFragment.Companion.DeleteAccountListener {

    private val userProfileViewModel by viewModels<UserProfileViewModel>()
    private val settingsViewModel by viewModels<SettingsViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enter_password)

        val sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val userId = sharedPref.getInt(CURRENT_USER_ID, -1)
        settingsViewModel.userId=userId

        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val etPassword = findViewById<TextInputEditText>(R.id.et_password)
        val tilPassword = findViewById<TextInputLayout>(R.id.til_password)


        findViewById<Button>(R.id.btn_continue).setOnClickListener {
            lifecycleScope.launch {
                if (etPassword.text.toString().isBlank()) {
                    tilPassword.isErrorEnabled = true
                    tilPassword.error = "Enter Password"
                }
                if (settingsViewModel.isPasswordValid(userId, etPassword.text.toString())) {
                    if (intent.getStringExtra("operation") == "change_password"){
                        finish()
                        Intent(
                            this@EnterPasswordActivity,
                            ResetPasswordActivity::class.java
                        ).apply {
                            putExtra("sender", "EnterPasswordActivity")
                            startActivity(this)
//                            finish()
                        }
                    }else if(intent.getStringExtra("operation")=="delete_account"){
                        DeleteAccountDialogFragment().show(supportFragmentManager,null)
                    }
                } else {
                    tilPassword.isErrorEnabled = true
                    tilPassword.error = "Incorrect Password"
                }
                tilPassword.clearFocus()
                etPassword.clearFocus()
                val imm: InputMethodManager =
                    getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(etPassword.windowToken, 0)
            }
        }

        etPassword.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                tilPassword.isErrorEnabled = false
            }
        }
    }

    override fun deleteAccount() {
        settingsViewModel.deleteAccount(settingsViewModel.userId)

        val sharedPref = getSharedPreferences(MY_SHARED_PREFERENCES, Context.MODE_PRIVATE)
        val editor=sharedPref.edit()
        editor.remove(CURRENT_USER_ID)
        editor.remove(CURRENT_USER_GENDER)
        editor.apply()

        finish()
        Intent(this,SignInActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            Snackbar.make(findViewById(R.id.et_password),"Account Deleted Successfully", Snackbar.LENGTH_SHORT).show()
//            Toast.makeText(this@EnterPasswordActivity,"Account Deleted Successfully",Toast.LENGTH_SHORT).show()
            startActivity(this)
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