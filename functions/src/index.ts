import * as admin from "firebase-admin";
import * as functions from "firebase-functions";

admin.initializeApp();
const db = admin.firestore();

type Alarm = {
  label: string;
  timeUtc: number;
  repeatDays: number[];
  enabled: boolean;
  nextFireUtc: number;
  updatedBy?: string;
};

function nextOccurrence(timeUtc: number, repeatDays: number[]): number {
  const now = Date.now();
  if (!repeatDays || repeatDays.length === 0) {
    return timeUtc < now ? timeUtc + 24*60*60*1000 : timeUtc;
  }
  const base = new Date(timeUtc);
  const hour = base.getUTCHours();
  const minute = base.getUTCMinutes();

  for (let i = 0; i < 8; i++) {
    const d = new Date();
    d.setUTCDate(d.getUTCDate() + i);
    const dow = ((d.getUTCDay() + 6) % 7) + 1; // 1..7 with Monday=1
    if (repeatDays.includes(dow)) {
      d.setUTCHours(hour, minute, 0, 0);
      if (d.getTime() > now) return d.getTime();
    }
  }
  return now + 24*60*60*1000;
}

exports.onAlarmWrite = functions.firestore
  .document("rooms/{roomId}/alarms/{alarmId}")
  .onWrite(async (change, context) => {
    const roomId = context.params.roomId;
    const alarmId = context.params.alarmId;
    const after = change.after.exists ? (change.after.data() as Alarm) : null;
    const before = change.before.exists ? (change.before.data() as Alarm) : null;
    if (!after) return;

    const next = after.enabled ? nextOccurrence(after.timeUtc, after.repeatDays || []) : 0;
    if (after.nextFireUtc !== next) {
      await change.after.ref.set({ nextFireUtc: next }, { merge: true });
    }

    await db.collection("rooms").doc(roomId).collection("events").add({
      type: before ? "UPDATE" : "CREATE",
      actorUid: after.updatedBy || "server",
      alarmId,
      ts: admin.firestore.FieldValue.serverTimestamp(),
      payload: { label: after.label, enabled: after.enabled, repeatDays: after.repeatDays || [] },
      ttl: admin.firestore.Timestamp.now(),
    });
  });
