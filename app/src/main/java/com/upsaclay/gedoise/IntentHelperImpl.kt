package com.upsaclay.gedoise

import android.content.Context
import android.content.Intent
import com.upsaclay.common.domain.IntentHelper
import com.upsaclay.gedoise.presentation.MainActivity

class IntentHelperImpl: IntentHelper {
    override fun getMainActivityIntent(context: Context): Intent = Intent(context, MainActivity::class.java)
}