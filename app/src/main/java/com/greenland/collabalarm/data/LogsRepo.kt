package com.greenland.collabalarm.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

object LogsRepo {
    fun recentEventsFlow(roomId: String) = callbackFlow<List<Map<String, Any?>>> {
        val ref = FirebaseFirestore.getInstance()
            .collection("rooms").document(roomId)
            .collection("events")
            .orderBy("ts", Query.Direction.DESCENDING)
            .limit(200)

        val reg = ref.addSnapshotListener { qs, e ->
            if (e != null) { trySend(emptyList()); return@addSnapshotListener }
            val items = qs?.documents?.map { it.data ?: emptyMap() } ?: emptyList()
            trySend(items)
        }
        awaitClose { reg.remove() }
    }
}
