package com.upsaclay.common.domain

import android.content.Context
import android.content.Intent

interface IntentHelper {
    fun getMainActivityIntent(context: Context): Intent
}