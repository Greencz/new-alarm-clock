@file:OptIn(ExperimentalMaterial3Api::class)

package com.greenland.collabalarm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.ExperimentalMaterial3Api


@Composable
fun EditAlarmScreen(nav: NavController) {
    var label by remember { mutableStateOf("Wake up") }
    var repeat by remember { mutableStateOf(setOf(1,2,3,4,5)) } // Mon-Fri

    Scaffold(
        topBar = { TopAppBar(title = { Text("Edit alarm") }) }
    ) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedTextField(value = label, onValueChange = { label = it }, label = { Text("Label") }, modifier = Modifier.fillMaxWidth())
            Text("Repeat days (chips here)")
            Button(onClick = { nav.popBackStack() }) { Text("Save") }
        }
    }
}
