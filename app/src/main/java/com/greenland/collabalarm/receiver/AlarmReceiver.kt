package com.greenland.collabalarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.greenland.collabalarm.service.AlarmService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val alarmId = intent.getStringExtra("alarmId") ?: return
        val svc = Intent(context, AlarmService::class.java).putExtra("alarmId", alarmId)
        ContextCompat.startForegroundService(context, svc)
    }
}
