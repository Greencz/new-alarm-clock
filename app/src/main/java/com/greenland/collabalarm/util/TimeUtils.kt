package com.greenland.collabalarm.util

import java.time.*

object TimeUtils {
    // 1=Mon..7=Sun
    fun nextFireFrom(hour: Int, minute: Int, repeatDays: Set<Int>, now: ZonedDateTime = ZonedDateTime.now()): Long {
        val today = now.toLocalDate()
        val base = LocalTime.of(hour, minute)
        val zone = now.zone
        if (repeatDays.isEmpty()) {
            var cand = ZonedDateTime.of(today, base, zone)
            if (cand.isBefore(now)) cand = cand.plusDays(1)
            return cand.toInstant().toEpochMilli()
        }
        for (i in 0..7) {
            val d = today.plusDays(i.toLong())
            val dow = d.dayOfWeek.value
            if (repeatDays.contains(dow)) {
                val cand = ZonedDateTime.of(d, base, zone)
                if (cand.isAfter(now)) return cand.toInstant().toEpochMilli()
            }
        }
        return ZonedDateTime.of(today.plusDays(1), base, zone).toInstant().toEpochMilli()
    }

    fun toPrettyTime(epochMs: Long, zone: ZoneId = ZoneId.systemDefault()): String {
        val zdt = Instant.ofEpochMilli(epochMs).atZone(zone)
        return "%02d:%02d".format(zdt.hour, zdt.minute)
    }
}
