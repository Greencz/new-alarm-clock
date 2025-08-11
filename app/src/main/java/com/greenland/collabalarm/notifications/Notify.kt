package com.greenland.collabalarm.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.greenland.collabalarm.R

object Notify {
    const val CH_ALARMS = "alarms"
    const val CH_ACTIVITY = "activity"

    fun initChannels(ctx: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nm = ctx.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val c1 = NotificationChannel(CH_ALARMS, "Alarm alerts", NotificationManager.IMPORTANCE_HIGH)
            val c2 = NotificationChannel(CH_ACTIVITY, "Activity", NotificationManager.IMPORTANCE_DEFAULT)
            nm.createNotificationChannel(c1)
            nm.createNotificationChannel(c2)
        }
    }

    fun showActivity(ctx: Context, title: String, body: String, id: Int = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()) {
        val n = NotificationCompat.Builder(ctx, CH_ACTIVITY)
            .setContentTitle(title)
            .setContentText(body)
            .setSmallIcon(R.drawable.ic_stat_notify)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(ctx).notify(id, n)
    }
}
