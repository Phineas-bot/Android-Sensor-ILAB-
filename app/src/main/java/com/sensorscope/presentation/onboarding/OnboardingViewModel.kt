package com.sensorscope.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val prefs: OnboardingPreferences
) : ViewModel() {

    val isOnboardingComplete: StateFlow<Boolean?> = prefs.isOnboardingComplete
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    fun markComplete() {
        viewModelScope.launch {
            prefs.markComplete()
        }
    }
}
