package com.teamsixers.vmail.ui.compose

import android.app.Activity
import com.teamsixers.vmail.ui.Task

/**
 * ComposeTask is the abstract task for task in composeActivity.
 */
abstract class ComposeTask : Task(){
    /**
     * call particular tasks of ComposeActivity
     * @param activity ComposeActivity
     */
    override fun doTask(activity: Activity) {
        if (activity is ComposeActivity) {
            doTask(activity)
            return
        }
        throw RuntimeException("compose task should only perform in composeActivity.")
    }

    abstract fun doTask(activity: ComposeActivity)
}