package com.dicoding.picodiploma.githubuserappfinal.view

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import com.dicoding.picodiploma.githubuserappfinal.R
import com.dicoding.picodiploma.githubuserappfinal.helper.AlarmHelper.cancelAlarm
import com.dicoding.picodiploma.githubuserappfinal.helper.AlarmHelper.setRepeatingAlarm
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : AppCompatActivity() {

    companion object {
        private const val PREFS_REMINDER = "reminder_pref"
    }
    private lateinit var preference: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setActionBar()
        setReminderMode(this)
        changeLanguageSetting()
    }

    private fun setActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = resources.getString(R.string.app_settings_title)
        }
    }

    private fun setReminderMode(context: Context) {
        preference = getSharedPreferences(PREFS_REMINDER, Context.MODE_PRIVATE)
        sw_reminder.apply {
            isChecked = preference.getBoolean(PREFS_REMINDER, false)
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    setRepeatingAlarm(context,resources.getString(R.string.message))
                    preference.edit { putBoolean(PREFS_REMINDER, true) }
                    Toast.makeText(context,
                        resources.getString(R.string.reminder_activated),
                        Toast.LENGTH_LONG).show()
                } else {
                    cancelAlarm(context)
                    preference.edit { putBoolean(PREFS_REMINDER, false) }
                    Toast.makeText(context,
                        resources.getString(R.string.reminder_deactivated),
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun changeLanguageSetting() {
        group_language_setting.setOnClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
