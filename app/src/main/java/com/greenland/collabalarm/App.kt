package com.greenland.collabalarm

import android.app.Application
import com.greenland.collabalarm.notifications.Notify

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Notify.initChannels(this)
    }
}
