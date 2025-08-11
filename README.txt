Fix Pack M1 – resolve unresolved references & duplicate packages

1) Decide your package name: using **com.greenland.collabalarm** (matches your errors).
2) Copy these files into your project (overwrite if asked):
   - app/src/main/java/com/greenland/collabalarm/model/Models.kt
   - app/src/main/java/com/greenland/collabalarm/data/Fire.kt
   - app/src/main/java/com/greenland/collabalarm/receiver/AlarmReceiver.kt
   - app/src/main/java/com/greenland/collabalarm/ui/theme/Theme.kt
   - app/src/main/java/com/greenland/collabalarm/ui/screens/HomeScreen.kt
   - app/src/main/java/com/greenland/collabalarm/ui/screens/EditAlarmScreen.kt
   - app/src/main/java/com/greenland/collabalarm/alarm/AlarmScheduler.kt
   - app/src/main/java/com/greenland/collabalarm/service/AlarmService.kt
   - app/src/main/java/com/greenland/collabalarm/ui/alarm/AlarmFullscreenActivity.kt
   - app/src/main/java/com/greenland/collabalarm/util/TimeUtils.kt

3) **Delete any duplicate files under `com/yourorg/collabalarm/**`** in your project, so you only have one package tree: `com.greenland.collabalarm`.

4) Ensure your AndroidManifest has receiver & service:
   <application ...>
       <service android:name=".collabalarm.service.AlarmService"
                android:exported="false"
                android:foregroundServiceType="mediaPlayback" />
       <receiver android:name=".collabalarm.receiver.AlarmReceiver"
                 android:exported="false" />
   </application>

   (Adjust the package path if needed; or fully qualify from the root package.)

5) Sync Gradle, then Build → Rebuild Project, then Run.

If MainActivity imports old package names, update imports to point to:
- com.greenland.collabalarm.ui.screens.HomeScreen
- com.greenland.collabalarm.ui.screens.EditAlarmScreen
