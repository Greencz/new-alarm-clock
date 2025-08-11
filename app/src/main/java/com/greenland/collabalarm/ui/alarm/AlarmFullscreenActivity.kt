package com.greenland.collabalarm.ui.alarm

import androidx.activity.ComponentActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.greenland.collabalarm.ui.theme.CollabTheme

class AlarmFullscreenActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollabTheme(amoled = true) {
                FullscreenAlarm(
                    onSnooze = { finish() /* TODO schedule snooze */ },
                    onDismiss = { finish() /* TODO log dismiss */ }
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
