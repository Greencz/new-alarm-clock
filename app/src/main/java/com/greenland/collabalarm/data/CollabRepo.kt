package com.greenland.collabalarm.data

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.greenland.collabalarm.model.Alarm
import com.greenland.collabalarm.core.DemoMode
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

data class Proposal(
    val id: String = "",
    val alarmId: String? = null,
    val payload: Map<String, Any?> = emptyMap(), // new values for alarm
    val createdBy: String = "",
    val createdAt: Long = System.currentTimeMillis()
)

object CollabRepo {
    // DEMO storage
    private val demo = MutableStateFlow<List<Proposal>>(emptyList())

    fun proposalsFlow(roomId: String) = if (DemoMode.DEMO) {
        demo.asStateFlow()
    } else callbackFlow<List<Proposal>> {
        val ref = FirebaseFirestore.getInstance()
            .collection("rooms").document(roomId).collection("proposals")
            .orderBy("createdAt", com.google.firebase.firestore.Query.Direction.DESCENDING)
        val reg = ref.addSnapshotListener { qs, e ->
            if (e != null) { trySend(emptyList()); return@addSnapshotListener }
            val list = qs?.documents?.map { d ->
                Proposal(
                    id = d.id,
                    alarmId = d.getString("alarmId"),
                    payload = (d.get("payload") as? Map<String, Any?>) ?: emptyMap(),
                    createdBy = d.getString("createdBy") ?: "",
                    createdAt = (d.get("createdAtMs") as? Number)?.toLong() ?: 0L
                )
            } ?: emptyList()
            trySend(list)
        }
        awaitClose { reg.remove() }
    }

    suspend fun submit(roomId: String, proposal: Proposal) {
        if (DemoMode.DEMO) {
            val id = UUID.randomUUID().toString()
            demo.value = listOf(proposal.copy(id = id)) + demo.value
            return
        }
        val db = FirebaseFirestore.getInstance()
        val ref = db.collection("rooms").document(roomId).collection("proposals").document()
        ref.set(mapOf(
            "alarmId" to proposal.alarmId,
            "payload" to proposal.payload,
            "createdBy" to proposal.createdBy,
            "createdAt" to FieldValue.serverTimestamp(),
            "createdAtMs" to System.currentTimeMillis()
        )).await()
    }

    suspend fun approve(roomId: String, proposal: Proposal) {
        if (DemoMode.DEMO) {
            // apply to local DemoRepo as an upsert
            val alarm = Alarm(
                id = proposal.alarmId ?: UUID.randomUUID().toString(),
                label = proposal.payload["label"] as? String ?: "Alarm",
                timeUtc = (proposal.payload["timeUtc"] as? Number)?.toLong() ?: System.currentTimeMillis(),
                repeatDays = (proposal.payload["repeatDays"] as? List<*>)?.mapNotNull { (it as? Number)?.toInt() }?.toSet() ?: emptySet(),
                enabled = (proposal.payload["enabled"] as? Boolean) ?: true,
                nextFireUtc = (proposal.payload["nextFireUtc"] as? Number)?.toLong() ?: System.currentTimeMillis()
            )
            DemoRepo.upsertAlarm(roomId, alarm)
            demo.value = demo.value.filterNot { it.id == proposal.id }
            return
        }
        val db = FirebaseFirestore.getInstance()
        // apply to target alarm
        val alarms = db.collection("rooms").document(roomId).collection("alarms")
        val id = proposal.alarmId ?: db.collection("_").document().id
        alarms.document(id).set(proposal.payload, com.google.firebase.firestore.SetOptions.merge()).await()
        // delete proposal
        db.collection("rooms").document(roomId).collection("proposals").document(proposal.id).delete().await()
    }

    suspend fun reject(roomId: String, id: String) {
        if (DemoMode.DEMO) {
            demo.value = demo.value.filterNot { it.id == id }
            return
        }
        FirebaseFirestore.getInstance()
            .collection("rooms").document(roomId).collection("proposals").document(id).delete().await()
    }
}
