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
fun SignInScreen(nav: NavController) {
    Scaffold(topBar = { TopAppBar(title = { Text("Sign in") }) }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = { /* TODO: launch Google sign-in */ }) { Text("Sign in with Google") }
        }
    }
}
