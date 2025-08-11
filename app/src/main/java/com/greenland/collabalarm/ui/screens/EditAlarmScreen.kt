@file:OptIn(ExperimentalMaterial3Api::class)

package com.greenland.collabalarm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.greenland.collabalarm.core.DemoMode
import com.greenland.collabalarm.data.DemoRepo
import com.greenland.collabalarm.data.Fire
import com.greenland.collabalarm.model.Alarm
import com.greenland.collabalarm.util.TimeUtils
import kotlinx.coroutines.launch

@Composable
fun EditAlarmScreen(nav: NavController) {
    var label by remember { mutableStateOf("Wake up") }
    var hour by remember { mutableStateOf(6) }
    var minute by remember { mutableStateOf(30) }
    var repeat by remember { mutableStateOf(setOf<Int>()) }

    val scope = rememberCoroutineScope()

    Scaffold(topBar = { TopAppBar(title = { Text("Edit alarm") }) }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(
                label = { Text("Name") },
                value = label,
                onValueChange = { label = it },
                modifier = Modifier.fillMaxWidth()
            )
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = hour.toString().padStart(2,'0'),
                    onValueChange = { it.toIntOrNull()?.let { v -> hour = v.coerceIn(0,23) } },
                    label = { Text("Hour") },
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = minute.toString().padStart(2,'0'),
                    onValueChange = { it.toIntOrNull()?.let { v -> minute = v.coerceIn(0,59) } },
                    label = { Text("Minute") },
                    modifier = Modifier.weight(1f)
                )
            }
            Text("Repeat on:")
            RepeatChips(selected = repeat, onChange = { repeat = it })

            Button(onClick = {
                scope.launch {
                    val roomId = if (DemoMode.DEMO) DemoRepo.ensureDefaultRoom() else Fire.ensureDefaultRoom()
                    val next = TimeUtils.nextFireFrom(hour, minute, repeat)
                    val alarm = Alarm(id = "", label = label, timeUtc = next, repeatDays = repeat, enabled = true, nextFireUtc = next)
                    if (DemoMode.DEMO) DemoRepo.upsertAlarm(roomId, alarm) else Fire.upsertAlarm(roomId, alarm)
                    nav.popBackStack()
                }
            }, modifier = Modifier.fillMaxWidth()) { Text("Save") }
        }
    }
}

@Composable
private fun RepeatChips(selected: Set<Int>, onChange: (Set<Int>) -> Unit) {
    val days = listOf("Mon" to 1, "Tue" to 2, "Wed" to 3, "Thu" to 4, "Fri" to 5, "Sat" to 6, "Sun" to 7)
    Column {
        Row {
            days.forEach { (label, value) ->
                val checked = selected.contains(value)
                FilterChip(
                    selected = checked,
                    onClick = { onChange(if (checked) selected - value else selected + value) },
                    label = { Text(label) },
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
                )
            }
        }
    }
}
