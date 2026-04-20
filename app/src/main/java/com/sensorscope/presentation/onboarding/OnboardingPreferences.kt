package com.sensorscope.presentation.onboarding

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OnboardingPreferences @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val ONBOARDING_COMPLETE = booleanPreferencesKey("onboarding_complete")

    val isOnboardingComplete: Flow<Boolean> = dataStore.data.map { prefs ->
        prefs[ONBOARDING_COMPLETE] ?: false
    }

    suspend fun markComplete() {
        dataStore.edit { prefs ->
            prefs[ONBOARDING_COMPLETE] = true
        }
    }
}
