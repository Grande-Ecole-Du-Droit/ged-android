package com.upsaclay.common.data

import android.content.Context
import androidx.work.WorkManager

abstract class WorkerLauncher(context: Context) {
    protected val workerManager = WorkManager.getInstance(context)

    abstract fun launch()
}