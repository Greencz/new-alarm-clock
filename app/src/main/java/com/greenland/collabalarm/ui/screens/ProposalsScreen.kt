@file:OptIn(ExperimentalMaterial3Api::class)

package com.greenland.collabalarm.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.greenland.collabalarm.data.CollabRepo
import com.greenland.collabalarm.data.Fire
import com.greenland.collabalarm.data.MembersRepo
import com.greenland.collabalarm.data.Proposal
import com.greenland.collabalarm.model.Role
import com.greenland.collabalarm.core.DemoMode
import kotlinx.coroutines.launch


@Composable
fun ProposalsScreen(nav: NavController) {
    var roomId by remember { mutableStateOf<String?>(null) }
    var proposals by remember { mutableStateOf<List<Proposal>>(emptyList()) }
    var myRole by remember { mutableStateOf(Role.VIEWER) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val rid = if (DemoMode.DEMO) "demo" else Fire.ensureDefaultRoom()
        roomId = rid
        CollabRepo.proposalsFlow(rid).collect { proposals = it }
        myRole = MembersRepo.myRole(rid)
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Change Requests") }) }) { pad ->
        LazyColumn(Modifier.padding(pad).padding(16.dp)) {
            items(proposals, key = { it.id }) { p ->
                ElevatedCard(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text("Proposal", style = MaterialTheme.typography.titleMedium)
                        Text(p.payload.toString(), style = MaterialTheme.typography.bodySmall)
                        Spacer(Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            val canApprove = myRole == Role.OWNER || myRole == Role.EDITOR
                            OutlinedButton(enabled = canApprove, onClick = {
                                val rid = roomId ?: return@OutlinedButton
                                scope.launch { CollabRepo.approve(rid, p) }
                            }) { Text("Approve") }
                            OutlinedButton(enabled = canApprove, onClick = {
                                val rid = roomId ?: return@OutlinedButton
                                scope.launch { CollabRepo.reject(rid, p.id) }
                            }) { Text("Reject") }
                        }
                    }
                }
            }
        }
    }
}
