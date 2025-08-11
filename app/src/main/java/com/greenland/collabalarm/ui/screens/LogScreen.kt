@file:OptIn(ExperimentalMaterial3Api::class)

package com.greenland.collabalarm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.greenland.collabalarm.core.DemoMode
import com.greenland.collabalarm.data.DemoRepo
import com.greenland.collabalarm.data.Fire
import com.greenland.collabalarm.data.LogsRepo
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun LogScreen(nav: NavController) {
    var items by remember { mutableStateOf<List<Map<String, Any?>>>(emptyList()) }

    LaunchedEffect(Unit) {
        if (DemoMode.DEMO) {
            DemoRepo.recentEventsFlow("demo").collect { list ->
                val cutoff = System.currentTimeMillis() - 24*60*60*1000
                items = list.filter { (it["tsMs"] as? Long ?: 0L) >= cutoff }
            }
        } else {
            val rid = Fire.ensureDefaultRoom()
            LogsRepo.recentEventsFlow(rid).collect { list ->
                val cutoff = System.currentTimeMillis() - 24*60*60*1000
                items = list.filter { (it["ts"] as? com.google.firebase.Timestamp)?.toDate()?.time ?: 0L >= cutoff }
            }
        }
    }

    val fmtHour = remember { SimpleDateFormat("HH:mm", Locale.getDefault()) }
    val grouped = remember(items) {
        items.groupBy {
            val ms = (it["tsMs"] as? Long) ?: (it["ts"] as? com.google.firebase.Timestamp)?.toDate()?.time ?: 0L
            fmtHour.format(Date(ms)).substring(0,2) + ":00"
        }.toSortedMap(compareByDescending { it })
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Activity (24h)") }) }) { pad ->
        LazyColumn(Modifier.padding(pad).padding(16.dp)) {
            grouped.forEach { (hour, list) ->
                item {
                    Text(hour, style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 8.dp))
                }
                items(list) { m ->
                    val type = (m["type"] as? String) ?: "EVENT"
                    val ts = (m["tsMs"] as? Long) ?: (m["ts"] as? com.google.firebase.Timestamp)?.toDate()?.time ?: 0L
                    ElevatedCard(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text(type, style = MaterialTheme.typography.titleMedium)
                            val payload = (m["payload"] as? Map<*, *>)?.entries?.joinToString { "${it.key}=${it.value}" } ?: ""
                            Text(payload, style = MaterialTheme.typography.bodySmall)
                            Text(Date(ts).toString(), style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }
            }
        }
    }
}
