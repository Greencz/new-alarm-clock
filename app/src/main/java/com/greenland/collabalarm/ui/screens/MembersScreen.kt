@file:OptIn(ExperimentalMaterial3Api::class)
package com.greenland.collabalarm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material3.ExperimentalMaterial3Api


@Composable
fun MembersScreen(nav: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Members") }) }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("Owner, Editors, Proposers, Viewers")
            ElevatedCard(Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp)) {
                    Text("Add / Manage Roles")
                }
            }
        }
    }
}
