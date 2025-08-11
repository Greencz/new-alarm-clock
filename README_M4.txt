Milestone 4 – UX polish + demo-safe notifications

WHAT'S NEW
- **"Turn on next time"**: when turning off a repeating alarm, snackbar shows action to enable it for next scheduled day.
- **Local notifications (demo-safe)**: shows a small notification when you enable/next-time an alarm.
- **Boot receiver scaffolding**: receiver to reschedule alarms after reboot (no-op in demo).
- **Notification channel init** in a custom Application class.

FILES TO COPY
- app/src/main/java/com/greenland/collabalarm/App.kt
- app/src/main/java/com/greenland/collabalarm/notifications/Notify.kt
- app/src/main/res/drawable/ic_stat_notify.xml
- app/src/main/java/com/greenland/collabalarm/receiver/BootReceiver.kt
- app/src/main/java/com/greenland/collabalarm/ui/screens/HomeScreen.kt (overwrite to add snackbar & notifications)

MANIFEST CHANGES
In AndroidManifest.xml:
1) Set the custom Application on the <application> tag:
   android:name=".App"

2) Add the BootReceiver inside <application>:
   (See AndroidManifest_additions.xml in this zip.)

PERMISSIONS
- For Android 13+, if you later enable real notifications, request POST_NOTIFICATIONS at runtime.
- In demo we only show local notifications which usually work without extra permission on pre-13 devices.

TEST
- Toggle a repeating alarm OFF → Snackbar appears with "Next time" → tap → alarm re-enables and shows a notification.
- Toggle ON → small notification "Alarm enabled".

NEXT (if you want)
- Real FCM push to collaborators (needs sign-in + token saving).
- Missed alarm detection & log entry if not dismissed within a window.
- Full reschedule on boot (non-demo).
