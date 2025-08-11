package com.greenland.collabalarm.data

import android.content.Context
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow

private val Context.dataStore by preferencesDataStore(name = "settings")

object SettingsRepo {
    private val KEY_SNOOZE_MIN = intPreferencesKey("snooze_min")

    fun snoozeMinutesFlow(ctx: Context): Flow<Int> =
        ctx.dataStore.data.map { it[KEY_SNOOZE_MIN] ?: 5 } // default 5 min

    suspend fun setSnoozeMinutes(ctx: Context, minutes: Int) {
        ctx.dataStore.edit { it[KEY_SNOOZE_MIN] = minutes.coerceIn(1, 30) }
    }
}
