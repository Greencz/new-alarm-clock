@file:OptIn(ExperimentalMaterial3Api::class)

package com.greenland.collabalarm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.Timestamp
import com.greenland.collabalarm.core.DemoMode
import com.greenland.collabalarm.data.DemoRepo
import com.greenland.collabalarm.data.Fire
import com.greenland.collabalarm.data.LogsRepo

@Composable
fun LogScreen(nav: NavController) {
    var items by remember { mutableStateOf<List<Map<String, Any?>>>(emptyList()) }

    LaunchedEffect(Unit) {
        if (DemoMode.DEMO) {
            DemoRepo.recentEventsFlow("demo").collect { list ->
                val cutoff = System.currentTimeMillis() - 24*60*60*1000
                items = list.filter {
                    val ts = (it["tsMs"] as? Long) ?: 0L
                    ts >= cutoff
                }
            }
        } else {
            val rid = Fire.ensureDefaultRoom()
            LogsRepo.recentEventsFlow(rid).collect { list ->
                val cutoff = System.currentTimeMillis() - 24*60*60*1000
                items = list.filter {
                    val ts = (it["ts"] as? Timestamp)?.toDate()?.time ?: 0L
                    ts >= cutoff
                }
            }
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Activity (last 24h)") }) }) { pad ->
        LazyColumn(Modifier.padding(pad).padding(16.dp)) {
            items(items) { m ->
                ElevatedCard(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text((m["type"] as? String) ?: "EVENT", style = MaterialTheme.typography.titleSmall)
                        val payload = (m["payload"] as? Map<*, *>)?.entries?.joinToString { "${it.key}=${it.value}" } ?: ""
                        Text(payload)
                        val tsStr = when (val ts = m["ts"]) {
                            is Timestamp -> ts.toDate().toString()
                            else -> java.util.Date((m["tsMs"] as? Long) ?: 0L).toString()
                        }
                        Text(tsStr)
                    }
                }
            }
        }
    }
}
