Milestone 2.5 – Demo mode (no sign-in preview) + Google Sign-In wiring

WHAT THIS GIVES YOU
- Run and use the app **without signing in** (preview mode).
- In-memory alarms + events so you can test UI flows.
- Proper Google Sign-In screen stub (replace YOUR_WEB_CLIENT_ID).

FILES TO COPY (overwrite if asked)
- app/src/main/java/com/greenland/collabalarm/core/DemoMode.kt
- app/src/main/java/com/greenland/collabalarm/data/DemoRepo.kt
- app/src/main/java/com/greenland/collabalarm/ui/screens/HomeScreen.kt
- app/src/main/java/com/greenland/collabalarm/ui/screens/EditAlarmScreen.kt
- app/src/main/java/com/greenland/collabalarm/ui/screens/LogScreen.kt
- app/src/main/java/com/greenland/collabalarm/ui/screens/SignInScreen.kt

HOW TO USE
- It's ON by default (DemoMode.DEMO = true). You can see Home, add alarms, toggle, see Logs.
- When ready to require auth, set DemoMode.DEMO = false.

MAKE GOOGLE SIGN-IN WORK (when you want)
1) Firebase Console → Authentication → Sign-in method → enable **Google**.
2) Firebase Console → Project Settings → Your apps (Android) → add SHA-1 and SHA-256:
   In Android Studio: View → Tool Windows → Gradle → :app → Tasks → android → **signingReport**
   Copy the **SHA-1** and **SHA-256** for your debug keystore into Firebase.
3) In the same settings page, copy the **Web client ID** (OAuth 2.0) and paste into SignInScreen:
   .requestIdToken("YOUR_WEB_CLIENT_ID")
4) Download the new **google-services.json** and place it at **app/google-services.json**.
5) Sync Gradle, run the app, tap **Sign in with Google**.
