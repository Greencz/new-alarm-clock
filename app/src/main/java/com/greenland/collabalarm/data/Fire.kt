package com.greenland.collabalarm.data

import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestoreSettings
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.greenland.collabalarm.model.Alarm

object Fire {
    val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val db: FirebaseFirestore by lazy {
        val f = FirebaseFirestore.getInstance()
        f.firestoreSettings = firestoreSettings { isPersistenceEnabled = true }
        f
    }

    suspend fun ensureDefaultRoom(): String {
        val uid = auth.currentUser?.uid ?: throw IllegalStateException("Not signed in")
        val roomId = uid
        val roomRef = db.collection("rooms").document(roomId)
        val snap = roomRef.get().await()
        if (!snap.exists()) {
            roomRef.set(mapOf(
                "ownerUid" to uid,
                "title" to "My Alarms",
                "members" to mapOf(uid to "owner"),
                "createdAt" to FieldValue.serverTimestamp()
            ), SetOptions.merge()).await()
        }
        return roomId
    }

    fun alarmsFlow(roomId: String) = callbackFlow<List<Alarm>> {
        val ref = db.collection("rooms").document(roomId).collection("alarms")
        val reg = ref.addSnapshotListener { qs, e ->
            if (e != null) { trySend(emptyList()); return@addSnapshotListener }
            val list = qs?.documents?.mapNotNull { it.toAlarm() } ?: emptyList()
            trySend(list)
        }
        awaitClose { reg.remove() }
    }

    suspend fun upsertAlarm(roomId: String, alarm: Alarm) {
        val uid = auth.currentUser?.uid ?: ""
        val ref = db.collection("rooms").document(roomId).collection("alarms")
            .document(if (alarm.id.isBlank()) db.collection("_").document().id else alarm.id)
        val data = hashMapOf(
            "label" to alarm.label,
            "timeUtc" to alarm.timeUtc,
            "repeatDays" to alarm.repeatDays.toList(),
            "enabled" to alarm.enabled,
            "nextFireUtc" to alarm.nextFireUtc,
            "updatedBy" to uid,
            "updatedAt" to FieldValue.serverTimestamp()
        )
        if (alarm.id.isBlank()) data["createdBy"] = uid
        ref.set(data, SetOptions.merge()).await()
        logEvent(roomId, ref.id, "UPDATE", mapOf("label" to alarm.label))
    }

    suspend fun logEvent(roomId: String, alarmId: String, type: String, payload: Map<String, Any?>) {
        val uid = auth.currentUser?.uid ?: ""
        db.collection("rooms").document(roomId).collection("events").add(mapOf(
            "type" to type,
            "actorUid" to uid,
            "alarmId" to alarmId,
            "ts" to FieldValue.serverTimestamp(),
            "payload" to payload,
            "ttl" to Timestamp.now()
        )).await()
    }
}

private fun DocumentSnapshot.toAlarm(): Alarm? {
    val id = id
    val label = getString("label") ?: ""
    val timeUtc = (get("timeUtc") as? Number)?.toLong() ?: 0L
    val enabled = getBoolean("enabled") ?: true
    val nextFireUtc = (get("nextFireUtc") as? Number)?.toLong() ?: 0L
    val repeatDays = (get("repeatDays") as? List<*>)?.mapNotNull { (it as? Number)?.toInt() }?.toSet() ?: emptySet()
    return Alarm(id, label, timeUtc, repeatDays, enabled, nextFireUtc)
}
