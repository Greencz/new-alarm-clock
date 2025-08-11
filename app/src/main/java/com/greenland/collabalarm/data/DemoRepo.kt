package com.greenland.collabalarm.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.greenland.collabalarm.model.Alarm
import java.util.UUID
import java.time.ZonedDateTime
import java.time.ZoneId

object DemoRepo {
    private val _alarms = MutableStateFlow<List<Alarm>>(listOf(
        Alarm(id = UUID.randomUUID().toString(), label = "Weekday Wake", timeUtc = System.currentTimeMillis(), repeatDays = setOf(1,2,3,4,5), enabled = true, nextFireUtc = System.currentTimeMillis() + 60*60*1000),
        Alarm(id = UUID.randomUUID().toString(), label = "Gym", timeUtc = System.currentTimeMillis(), repeatDays = setOf(2,4,6), enabled = false, nextFireUtc = System.currentTimeMillis() + 2*60*60*1000)
    ))
    private val _events = MutableStateFlow<List<Map<String, Any?>>>(emptyList())

    fun alarmsFlow(roomId: String) = _alarms.asStateFlow()
    suspend fun ensureDefaultRoom(): String = "demo"

    suspend fun upsertAlarm(roomId: String, alarm: Alarm) {
        val id = if (alarm.id.isBlank()) UUID.randomUUID().toString() else alarm.id
        val updated = alarm.copy(id = id)
        _alarms.value = _alarms.value.filterNot { it.id == id } + updated
        logEvent(roomId, id, if (alarm.id.isBlank()) "CREATE" else "UPDATE", mapOf("label" to alarm.label, "enabled" to alarm.enabled))
    }

    suspend fun logEvent(roomId: String, alarmId: String, type: String, payload: Map<String, Any?>) {
        val ev = mapOf(
            "type" to type,
            "alarmId" to alarmId,
            "payload" to payload,
            "tsMs" to System.currentTimeMillis()
        )
        _events.value = listOf(ev) + _events.value
    }

    fun recentEventsFlow(roomId: String) = _events.asStateFlow()
}
