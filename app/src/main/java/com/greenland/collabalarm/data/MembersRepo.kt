package com.greenland.collabalarm.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.channels.awaitClose
import com.greenland.collabalarm.model.Role
import com.greenland.collabalarm.core.DemoMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object MembersRepo {
    // DEMO in-memory
    private val demoMembers = MutableStateFlow(
        listOf(
            mapOf("uid" to "owner", "displayName" to "You", "role" to Role.OWNER.name),
            mapOf("uid" to "friend1", "displayName" to "Friend (viewer)", "role" to Role.VIEWER.name)
        )
    )

    fun membersFlow(roomId: String) = if (DemoMode.DEMO) {
        demoMembers.asStateFlow()
    } else callbackFlow<List<Map<String, Any?>>> {
        val ref = FirebaseFirestore.getInstance()
            .collection("rooms").document(roomId)
            .collection("members")
        val reg = ref.addSnapshotListener { qs, e ->
            if (e != null) { trySend(emptyList()); return@addSnapshotListener }
            val list = qs?.documents?.map { d ->
                val data = d.data ?: emptyMap<String, Any?>()
                data + mapOf("uid" to d.id)
            } ?: emptyList()
            trySend(list)
        }
        awaitClose { reg.remove() }
    }

    suspend fun setRole(roomId: String, uid: String, role: Role) {
        if (DemoMode.DEMO) {
            val others = demoMembers.value.filterNot { it["uid"] == uid }
            demoMembers.value = others + mapOf("uid" to uid, "displayName" to uid, "role" to role.name)
            return
        }
        val db = FirebaseFirestore.getInstance()
        db.collection("rooms").document(roomId).collection("members").document(uid)
            .set(mapOf("role" to role.name, "uid" to uid, "updatedAt" to FieldValue.serverTimestamp()), com.google.firebase.firestore.SetOptions.merge())
            .await()
    }

    suspend fun addMemberByEmail(roomId: String, email: String) {
        if (DemoMode.DEMO) {
            val uid = email
            val others = demoMembers.value.filterNot { it["uid"] == uid }
            demoMembers.value = others + mapOf("uid" to uid, "displayName" to email, "role" to Role.PROPOSER.name)
            return
        }
        // In real mode you'd look up by email via your own mapping or ask user for UID
        // For now we create a placeholder doc keyed by email string
        setRole(roomId, email, Role.PROPOSER)
    }

    suspend fun myRole(roomId: String): Role {
        if (DemoMode.DEMO) return Role.OWNER
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return Role.VIEWER
        val snap = FirebaseFirestore.getInstance().collection("rooms").document(roomId)
            .collection("members").document(uid).get().await()
        val r = snap.getString("role") ?: return Role.VIEWER
        return Role.valueOf(r)
    }
}
