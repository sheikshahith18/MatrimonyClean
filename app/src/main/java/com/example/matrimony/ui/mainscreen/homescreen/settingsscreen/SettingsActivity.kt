package com.example.matrimony.ui.mainscreen.homescreen.settingsscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.preference.PreferenceFragmentCompat
import com.example.matrimony.R
import com.example.matrimony.ui.mainscreen.homescreen.settingsscreen.preffrag.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        if (findViewById<FrameLayout>(R.id.settings_frame) != null) {
            if (savedInstanceState != null) {
                return
            }
            val settingsFragment = SettingsFragment().apply {
                arguments = Bundle().apply {
                    putString(PreferenceFragmentCompat.ARG_PREFERENCE_ROOT, "settings")
                }
            }

            supportFragmentManager.beginTransaction().apply {
                replace(R.id.settings_frame, settingsFragment)
                commit()
            }
            supportFragmentManager.popBackStack()
        }

//        findViewById<TextView>(R.id.tv_privacy_settings).setOnClickListener {
//            val intent = Intent(this, PrivacySettingsActivity::class.java)
//            startActivity(intent)
//        }
//
//        findViewById<TextView>(R.id.tv_account_settings).setOnClickListener {
//            val intent = Intent(this, AccountSettingsActivity::class.java)
//            startActivity(intent)
//        }

        setSupportActionBar(findViewById<Toolbar>(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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