package com.greenland.collabalarm.ui.alarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import com.greenland.collabalarm.ui.theme.CollabTheme
import com.greenland.collabalarm.data.Fire

class AlarmFullscreenActivity : ComponentActivity() {
    private var alarmId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        alarmId = intent.getStringExtra("alarmId") ?: ""
        setContent {
            CollabTheme() {
                FullscreenAlarm(
                    onSnooze = {
                        lifecycleScope.launch {
                            val rid = Fire.ensureDefaultRoom()
                            Fire.logEvent(rid, alarmId, "SNOOZE", emptyMap())
                        }
                        finish()
                    },
                    onDismiss = {
                        lifecycleScope.launch {
                            val rid = Fire.ensureDefaultRoom()
                            Fire.logEvent(rid, alarmId, "DISMISS", emptyMap())
                        }
                        finish()
                    }
                )
            }
        }
    }
}

@Composable
fun FullscreenAlarm(onSnooze: ()->Unit, onDismiss: ()->Unit) {
    Surface {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text("Alarm ringing", style = MaterialTheme.typography.headlineLarge)
                Button(onClick = onSnooze) { Text("Snooze 5 min") }
                OutlinedButton(onClick = onDismiss) { Text("I'm up") }
            }
        }
    }
}
