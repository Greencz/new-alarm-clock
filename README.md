# Collab alarm clock (starter)

Dark-first collaborative alarm clock with Google sign-in, repeat days, 5-min snooze, proposals (restricted editor), 24h logs, and notifications.

## Quick start
1. Open this folder in Android Studio.
2. Add your Firebase config: download **google-services.json** to `app/` and enable Auth (Google), Firestore, and FCM in the Firebase console.
3. Build & run.

## What’s included
- Jetpack Compose UI (Home, Edit, Members, Logs, Full-screen alarm)
- Exact alarms via `setAlarmClock()` + foreground service
- Roles: owner/editor/proposer/viewer (UI stubs)
- Proposals/decisions & logs (Cloud Functions stubs)
- Dark theme + optional AMOLED 

## Next steps
- Wire Firestore repositories and snapshot listeners
- Implement proposals approval UI and notifications
- Add local Room DB + WorkManager prune for 24h logs
