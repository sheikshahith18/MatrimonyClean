package com.example.matrimony.ui.mainscreen.homescreen.settingsscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.matrimony.R

class AccountSettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account_settings)


        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        findViewById<TextView>(R.id.tv_change_password).setOnClickListener {
            Intent(this, EnterPasswordActivity::class.java).apply {
                putExtra("operation","change_password")
                startActivity(this)
            }
        }

        findViewById<TextView>(R.id.tv_delete_account).setOnClickListener {
            Intent(this, EnterPasswordActivity::class.java).apply {
                putExtra("operation","delete_account")
                startActivity(this)
            }
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