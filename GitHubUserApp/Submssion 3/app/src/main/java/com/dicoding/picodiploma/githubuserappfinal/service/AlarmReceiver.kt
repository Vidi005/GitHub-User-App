package com.dicoding.picodiploma.githubuserappfinal.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.dicoding.picodiploma.githubuserappfinal.R
import com.dicoding.picodiploma.githubuserappfinal.helper.AlarmHelper.showAlarmNotification

class AlarmReceiver : BroadcastReceiver() {

    companion object {
        const val CHANNEL_ID = "Channel_1"
        const val CHANNEL_NAME = "Alarm Manager Channel"
        const val NOTIFICATION_ID = 1
    }

    override fun onReceive(context: Context, intent: Intent) {
        val title = context.resources.getString(R.string.app_name)
        val message = context.resources.getString(R.string.message)
        showAlarmNotification(context, title, message)
    }
}
