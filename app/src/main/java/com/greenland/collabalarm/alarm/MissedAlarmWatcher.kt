package com.greenland.collabalarm.alarm

import android.content.Context
import kotlinx.coroutines.*
import com.greenland.collabalarm.core.DemoMode
import com.greenland.collabalarm.data.DemoRepo

object MissedAlarmWatcher {
    private val scope = CoroutineScope(Dispatchers.Default)

    fun watch(ctx: Context, roomId: String, alarmId: String, fireTs: Long) {
        if (!DemoMode.DEMO) return // We'll move this to server/worker in real mode
        scope.launch {
            delay(2 * 60 * 1000) // 2 minutes
            // If in-memory we can't know, but we log a MISSED for demo flow
            DemoRepo.logEvent(roomId, alarmId, "MISSED", emptyMap())
        }
    }
}
