package com.upsaclay.authentication.presentation.registration.secondregistration

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import okhttp3.internal.immutableListOf

class SecondRegistrationViewModel: ViewModel() {
    val schoolLevels = immutableListOf("GED 1", "GED 2", "GED 3", "GED 4")
    private val _schoolLevel = MutableStateFlow(schoolLevels[0])
    val schoolLevel: StateFlow<String> = _schoolLevel

    fun onSchoolLevelChange(schoolLevel: String) {
        _schoolLevel.value = schoolLevel
    }
}