@file:OptIn(ExperimentalMaterial3Api::class)
package com.greenland.collabalarm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.ExperimentalMaterial3Api


@Composable
fun HomeScreen(nav: NavController) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Collab alarm clock") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { nav.navigate("edit") }) {
                Text("+")
            }
        }
    ) { pad ->
        Box(Modifier.fillMaxSize().padding(pad)) {
            LazyColumn(Modifier.fillMaxSize().padding(16.dp)) {
                items(3) { idx ->
                    ElevatedCard(Modifier.fillParentMaxWidth().padding(bottom = 12.dp)) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text("06:30", style = MaterialTheme.typography.headlineLarge)
                                Text("Tue, Thu • Wake up")
                            }
                            Switch(checked = true, onCheckedChange = {})
                        }
                    }
                }
            }
        }
    }
}
