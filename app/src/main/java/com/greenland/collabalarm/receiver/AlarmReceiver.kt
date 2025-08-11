package com.greenland.collabalarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.greenland.collabalarm.service.AlarmService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "ALARM_FIRE") {
            val alarmId = intent.getStringExtra("alarmId") ?: ""
            val svc = Intent(context, AlarmService::class.java).apply {
                putExtra("alarmId", alarmId)
            }
            context.startForegroundService(svc)
        }
    }
}
