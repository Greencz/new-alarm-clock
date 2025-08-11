@file:OptIn(ExperimentalMaterial3Api::class)

package com.greenland.collabalarm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.greenland.collabalarm.data.SettingsRepo
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(nav: NavController) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()
    val snooze by SettingsRepo.snoozeMinutesFlow(ctx).collectAsState(initial = 5)
    var temp by remember { mutableStateOf(snooze.toString()) }

    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Default snooze length (minutes)")
            OutlinedTextField(
                value = temp,
                onValueChange = { temp = it.filter { ch -> ch.isDigit() }.take(2) },
                modifier = Modifier.width(120.dp)
            )
            Button(onClick = {
                scope.launch { SettingsRepo.setSnoozeMinutes(ctx, temp.toIntOrNull() ?: 5) }
            }) { Text("Save") }
        }
    }
}
