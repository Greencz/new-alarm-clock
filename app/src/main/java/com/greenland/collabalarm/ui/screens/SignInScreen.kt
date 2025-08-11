@file:OptIn(ExperimentalMaterial3Api::class)

package com.greenland.collabalarm.ui.screens

import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.greenland.collabalarm.core.DemoMode

@Composable
fun SignInScreen(nav: NavController) {
    val ctx = LocalContext.current
    val auth = remember { FirebaseAuth.getInstance() }
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("YOUR_WEB_CLIENT_ID")
            .requestEmail()
            .build()
    }
    val client = remember { GoogleSignIn.getClient(ctx, gso) }

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { res ->
        if (res.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(res.data)
            try {
                val account = task.result
                val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                auth.signInWithCredential(credential).addOnCompleteListener {
                    nav.popBackStack()
                }
            } catch (e: Exception) {
                // ignore for demo
            }
        }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Sign in") }) }) { pad ->
        Column(Modifier.padding(pad).fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Button(onClick = {
                launcher.launch(client.signInIntent)
            }) { Text("Sign in with Google") }

            Spacer(Modifier.height(16.dp))

            if (DemoMode.DEMO) {
                OutlinedButton(onClick = { nav.navigate("home") }) {
                    Text("Continue without sign-in (Preview)")
                }
            }
        }
    }
}
