Milestone 3 – Roles, restricted edits (proposals), members management

WHAT'S NEW
- Roles: OWNER, EDITOR, PROPOSER, VIEWER
- Members screen to invite and set roles
- Restricted edits: PROPOSER cannot change alarms directly → they submit a proposal
- Proposals screen: OWNERS/EDITORS can approve/reject; approval applies change
- Works in DEMO mode (no sign-in) with in-memory members/proposals

FILES TO COPY
- app/src/main/java/com/greenland/collabalarm/model/Roles.kt
- app/src/main/java/com/greenland/collabalarm/data/MembersRepo.kt
- app/src/main/java/com/greenland/collabalarm/data/CollabRepo.kt
- app/src/main/java/com/greenland/collabalarm/ui/screens/MembersScreen.kt
- app/src/main/java/com/greenland/collabalarm/ui/screens/ProposalsScreen.kt
- app/src/main/java/com/greenland/collabalarm/ui/screens/EditAlarmScreen.kt (overwrite to enable proposal flow)

NAVIGATION
Add routes in your NavHost:
  composable("members") { MembersScreen(navController) }
  composable("proposals") { ProposalsScreen(navController) }

Add buttons to reach them (e.g., in Home top app bar actions):
  TextButton(onClick = { navController.navigate("members") }) { Text("Members") }
  TextButton(onClick = { navController.navigate("proposals") }) { Text("Proposals") }

FIRESTORE RULES (for real sign-in later)
Use firestore.rules.roles.txt when you enable auth to enforce roles.

NOTES
- Notifications: once you enable FCM tokens per member, we can send notifications on:
  - Alarm updated
  - Proposal created / approved / rejected
  - Snooze / Dismiss / Missed
- In DEMO mode, everything is purely local so you can test flows without Firebase.
