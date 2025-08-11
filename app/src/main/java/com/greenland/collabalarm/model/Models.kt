package com.greenland.collabalarm.model

data class Alarm(
    val id: String = "",
    val label: String = "",
    val timeUtc: Long = 0L,
    val repeatDays: Set<Int> = emptySet(),
    val enabled: Boolean = true,
    val nextFireUtc: Long = 0L
)
