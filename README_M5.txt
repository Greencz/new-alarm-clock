Milestone 5 – Dark theme, Settings (default snooze), viewer UI lock scaffolding, log polish, missed detection (demo-safe)

WHAT'S NEW
- **Dark theme** polish via Material 3 dark colors (`ui/theme/`).
- **Settings** screen to set default snooze minutes (DataStore-backed).
- **Viewer UI lock** scaffolding (banner + notes to disable FAB in viewer mode).
- **Log screen** grouped by hour and cleaner cards.
- **Missed-alarm detection** (demo): logs MISSED 2 minutes after a fire if untouched.

FILES TO COPY
- app/src/main/java/com/greenland/collabalarm/data/SettingsRepo.kt
- app/src/main/java/com/greenland/collabalarm/ui/screens/SettingsScreen.kt
- app/src/main/java/com/greenland/collabalarm/ui/theme/Colors.kt
- app/src/main/java/com/greenland/collabalarm/ui/theme/Theme.kt
- app/src/main/java/com/greenland/collabalarm/ui/ReadOnlyBanner.kt
- app/src/main/java/com/greenland/collabalarm/alarm/MissedAlarmWatcher.kt
- app/src/main/java/com/greenland/collabalarm/ui/screens/LogScreen.kt

NAVIGATION
Add to your NavHost:
  composable("settings") { SettingsScreen(navController) }

Add a top bar action in Home:
  TextButton(onClick = { navController.navigate("settings") }) { Text("Settings") }

THEME
Wrap your top-level `setContent` with `CollabTheme { ... }` if not already.

NOTES
- MissedAlarmWatcher is demo-only. We'll move to a Worker or server logic with FCM when auth is enabled.
- To fully lock viewer UI, check `myRole` in Home and hide/disable FAB + switches for `VIEWER`.
