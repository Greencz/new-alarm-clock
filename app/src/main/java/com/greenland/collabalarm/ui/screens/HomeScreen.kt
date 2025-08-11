@file:OptIn(ExperimentalMaterial3Api::class)

package com.greenland.collabalarm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.greenland.collabalarm.core.DemoMode
import com.greenland.collabalarm.data.DemoRepo
import com.greenland.collabalarm.data.Fire
import com.greenland.collabalarm.model.Alarm
import com.greenland.collabalarm.alarm.AlarmScheduler
import com.greenland.collabalarm.util.TimeUtils
import com.greenland.collabalarm.notifications.Notify
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(nav: NavController) {
    val auth = remember { FirebaseAuth.getInstance() }
    val scope = rememberCoroutineScope()
    var alarms by remember { mutableStateOf<List<Alarm>>(emptyList()) }
    var roomId by remember { mutableStateOf<String?>(null) }
    val user = auth.currentUser
    val ctx = LocalContext.current
    val host = remember { SnackbarHostState() }

    LaunchedEffect(user) {
        if (DemoMode.DEMO) {
            roomId = DemoRepo.ensureDefaultRoom()
            DemoRepo.alarmsFlow("demo").collect { alarms = it }
        } else {
            if (user == null) return@LaunchedEffect
            val rid = Fire.ensureDefaultRoom()
            roomId = rid
            Fire.alarmsFlow(rid).collect { alarms = it }
        }
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Collab alarm clock") }, actions = {
            TextButton(onClick = { nav.navigate("logs") }) { Text("Logs") }
            TextButton(onClick = { nav.navigate("members") }) { Text("Members") }
            TextButton(onClick = { nav.navigate("proposals") }) { Text("Proposals") }
            TextButton(onClick = { nav.navigate("settings") }) { Text("Settings") }
        }) },
        floatingActionButton = {
            if (DemoMode.DEMO || (user != null && roomId != null)) {
                FloatingActionButton(onClick = { nav.navigate("edit") }) { Text("+") }
            }
        },
        snackbarHost = { SnackbarHost(hostState = host) }
    ) { pad ->
        Box(Modifier.fillMaxSize().padding(pad)) {
            when {
                !DemoMode.DEMO && user == null -> {
                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Please sign in")
                        Spacer(Modifier.height(12.dp))
                        Button(onClick = { nav.navigate("signin") }) { Text("Go to sign-in") }
                    }
                }
                alarms.isEmpty() -> {
                    Column(Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("No alarms yet")
                    }
                }
                else -> {
                    LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
                        items(alarms, key = { it.id }) { a ->
                            ElevatedCard(Modifier.fillParentMaxWidth().padding(bottom = 12.dp)) {
                                Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Column(Modifier.weight(1f)) {
                                        Text(a.label.ifBlank { "Alarm" }, style = MaterialTheme.typography.titleLarge)
                                        Text("${TimeUtils.toPrettyTime(a.nextFireUtc)} • ${if (a.repeatDays.isEmpty()) "one-shot" else "repeat " + a.repeatDays.joinToString()}")
                                    }
                                    Switch(
                                        checked = a.enabled,
                                        onCheckedChange = { on ->
                                            scope.launch {
                                                val rid = roomId ?: return@launch
                                                val updated = a.copy(enabled = on)
                                                if (DemoMode.DEMO) DemoRepo.upsertAlarm(rid, updated) else Fire.upsertAlarm(rid, updated)
                                                if (on) {
                                                    AlarmScheduler.schedule(ctx, updated)
                                                    Notify.showActivity(ctx, "Alarm enabled", a.label)
                                                } else {
                                                    AlarmScheduler.cancel(ctx, a.id)
                                                    // If repeating, offer "Turn on next time"
                                                    if (a.repeatDays.isNotEmpty()) {
                                                        val res = host.showSnackbar(
                                                            message = "Alarm turned off",
                                                            actionLabel = "Next time",
                                                            withDismissAction = true
                                                        )
                                                        if (res == SnackbarResult.ActionPerformed) {
                                                            val nextEnabled = updated.copy(enabled = true)
                                                            if (DemoMode.DEMO) DemoRepo.upsertAlarm(rid, nextEnabled) else Fire.upsertAlarm(rid, nextEnabled)
                                                            Notify.showActivity(ctx, "Will ring next time", a.label)
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
