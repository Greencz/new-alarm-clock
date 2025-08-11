package com.greenland.collabalarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.greenland.collabalarm.core.DemoMode

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            if (DemoMode.DEMO) return
            // TODO: Query alarms and reschedule (when Firestore is available)
        }
    }
}
