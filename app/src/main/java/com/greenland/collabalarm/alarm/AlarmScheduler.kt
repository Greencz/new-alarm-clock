package com.greenland.collabalarm.alarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.greenland.collabalarm.receiver.AlarmReceiver
import com.greenland.collabalarm.model.Alarm

object AlarmScheduler {
    fun schedule(ctx: Context, alarm: Alarm) {
        val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(ctx, AlarmReceiver::class.java).apply {
            action = "ALARM_FIRE"
            putExtra("alarmId", alarm.id)
        }
        val pi = PendingIntent.getBroadcast(
            ctx, alarm.id.hashCode(), intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val info = AlarmManager.AlarmClockInfo(alarm.nextFireUtc, pi)
        am.setAlarmClock(info, pi)
    }

    fun cancel(ctx: Context, alarmId: String) {
        val intent = Intent(ctx, AlarmReceiver::class.java).apply { action = "ALARM_FIRE" }
        val pi = PendingIntent.getBroadcast(
            ctx, alarmId.hashCode(), intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        if (pi != null) {
            val am = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            am.cancel(pi)
        }
    }
}
