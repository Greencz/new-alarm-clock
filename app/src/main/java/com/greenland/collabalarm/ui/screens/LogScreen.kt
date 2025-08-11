@file:OptIn(ExperimentalMaterial3Api::class)
package com.greenland.collabalarm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.ExperimentalMaterial3Api


@Composable
fun LogScreen(nav: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Activity (24h)") }) }) { pad ->
        LazyColumn(Modifier.padding(pad).padding(16.dp)) {
            items(5) {
                ElevatedCard(Modifier.fillParentMaxWidth().padding(bottom = 12.dp)) {
                    Column(Modifier.padding(16.dp)) {
                        Text("06:35 Snoozed (by you)")
                        Text("06:45 Dismissed")
                    }
                }
            }
        }
    }
}
