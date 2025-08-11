package com.greenland.collabalarm.model

data class Alarm(
    val id: String = "",
    val label: String = "",
    val timeUtc: Long = 0L,
    val repeatDays: Set<Int> = emptySet(), // 1..7 (Mon..Sun)
    val enabled: Boolean = true,
    val nextFireUtc: Long = 0L
)

data class Proposal(
    val id: String = "",
    val alarmId: String = "",
    val diff: Map<String, Any?> = emptyMap(),
    val proposedBy: String = "",
    val status: String = "PENDING"
)

data class EventLog(
    val id: String = "",
    val type: String = "",
    val actorUid: String = "",
    val alarmId: String = "",
    val ts: Long = 0L,
    val payload: Map<String, Any?> = emptyMap()
)
