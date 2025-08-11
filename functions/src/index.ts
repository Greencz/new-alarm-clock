// Cloud Functions stubs for proposals/logging
import * as admin from "firebase-admin";
import * as functions from "firebase-functions";

admin.initializeApp();
const db = admin.firestore();

export const proposeEdit = functions.https.onCall(async (data, context) => {
  const { roomId, alarmId, diff } = data;
  const uid = context.auth?.uid;
  if (!uid) throw new functions.https.HttpsError("unauthenticated", "Sign in.");
  // TODO: verify role, write proposal, notify owners
  return { ok: true };
});

export const decideProposal = functions.https.onCall(async (data, context) => {
  const { roomId, proposalId, accept } = data;
  const uid = context.auth?.uid;
  if (!uid) throw new functions.https.HttpsError("unauthenticated", "Sign in.");
  // TODO: verify role, apply diff if accepted, notify proposer
  return { ok: true };
});

export const logEvent = functions.https.onCall(async (data, context) => {
  const { roomId, alarmId, type, payload } = data;
  const uid = context.auth?.uid;
  if (!uid) throw new functions.https.HttpsError("unauthenticated", "Sign in.");
  // TODO: append /events with TTL
  return { ok: true };
});
