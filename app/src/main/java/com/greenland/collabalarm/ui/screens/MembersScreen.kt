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
import com.greenland.collabalarm.data.MembersRepo
import com.greenland.collabalarm.data.Fire
import com.greenland.collabalarm.model.Role
import com.greenland.collabalarm.core.DemoMode
import kotlinx.coroutines.launch

@Composable
fun MembersScreen(nav: NavController) {
    var roomId by remember { mutableStateOf<String?>(null) }
    var members by remember { mutableStateOf<List<Map<String, Any?>>>(emptyList()) }
    var email by remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        val rid = if (DemoMode.DEMO) "demo" else Fire.ensureDefaultRoom()
        roomId = rid
        MembersRepo.membersFlow(rid).collect { members = it }
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Members & Roles") }) }) { pad ->
        Column(Modifier.padding(pad).padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Invite by email / uid") }, modifier = Modifier.weight(1f))
                Button(onClick = {
                    val rid = roomId ?: return@Button
                    scope.launch { MembersRepo.addMemberByEmail(rid, email.trim()); email = "" }
                }) { Text("Invite") }
            }
            Spacer(Modifier.height(12.dp))
            LazyColumn {
                items(members, key = { it["uid"] as String }) { m ->
                    val uid = m["uid"] as String
                    val roleName = (m["role"] as? String) ?: Role.VIEWER.name
                    val current = Role.valueOf(roleName)
                    ElevatedCard(Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        Column(Modifier.padding(12.dp)) {
                            Text(m["displayName"] as? String ?: uid, style = MaterialTheme.typography.titleMedium)
                            Text("uid: " + uid, style = MaterialTheme.typography.labelSmall)
                            Spacer(Modifier.height(8.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Role.values().forEach { r ->
                                    FilterChip(
                                        selected = current == r,
                                        onClick = {
                                            val rid = roomId ?: return@FilterChip
                                            scope.launch { MembersRepo.setRole(rid, uid, r) }
                                        },
                                        label = { Text(r.name) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
