package com.greenland.collabalarm.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class PushService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        // TODO: Show notifications for proposals/decisions/updates/events
    }
}
