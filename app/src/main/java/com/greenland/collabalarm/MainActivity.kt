package com.greenland.collabalarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.greenland.collabalarm.ui.screens.*
import com.greenland.collabalarm.ui.theme.CollabTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CollabTheme() {
                AppNav()
            }
        }
    }
}

@Composable
fun AppNav() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "home") {
        composable("home") { HomeScreen(nav) }
        composable("edit") { EditAlarmScreen(nav) }
        composable("members") { MembersScreen(nav) }
        composable("proposals") { ProposalsScreen(nav) }
        composable("logs") { LogScreen(nav) }
        composable("settings") { SettingsScreen(nav) }
        composable("signin") { SignInScreen(nav) }
    }
}

@Preview
@Composable
fun PreviewApp() {
    CollabTheme() {
        androidx.compose.material3.Text("Collab alarm clock", color = MaterialTheme.colorScheme.onBackground)
    }
}
