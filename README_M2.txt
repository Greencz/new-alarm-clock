Milestone 2 – Drop-in bundle (Logs + basic server assist)

WHAT'S INSIDE
- Android:
  - data/LogsRepo.kt (Firestore stream of last 200 events)
  - ui/screens/LogScreen.kt (simple 24h activity timeline)
  - ui/alarm/AlarmFullscreenActivity.kt (logs SNOOZE/DISMISS)
- Firebase:
  - firestore.rules.txt (loose rules for quick testing)
  - functions/ (TypeScript Cloud Function to recompute nextFireUtc + log updates)

HOW TO APPLY
1) Copy Android files into:
   - app/src/main/java/com/greenland/collabalarm/data/LogsRepo.kt
   - app/src/main/java/com/greenland/collabalarm/ui/screens/LogScreen.kt
   - app/src/main/java/com/greenland/collabalarm/ui/alarm/AlarmFullscreenActivity.kt (overwrite)

2) Add a route to your NavHost (MainActivity):
   navController.navigate("logs") from a menu/button, and add:
     composable("logs") { LogScreen(navController) }

3) OPTIONAL – Firestore rules (quick testing):
   Paste firestore.rules.txt into Firebase Console → Firestore → Rules and publish.
   (We'll harden roles next milestone.)

4) OPTIONAL – Cloud Functions:
   - Install Firebase CLI (if not yet): https://firebase.google.com/docs/cli
   - In your project root, create a 'functions' folder and copy this one in.
   - Run:
       cd functions
       npm i
       npm run build
       firebase deploy --only functions
   This will recompute nextFireUtc on server when alarms change and append UPDATE/CREATE events.

5) Run the app, create/modify an alarm, then open the Logs screen.
   You should see CREATE/UPDATE entries, and SNOOZE/DISMISS when you use the full-screen alarm.

NOTES
- Push notifications to members will come in the next milestone (we'll add FCM token saving + member roles).
- If your package name differs, update the 'package' lines accordingly.
