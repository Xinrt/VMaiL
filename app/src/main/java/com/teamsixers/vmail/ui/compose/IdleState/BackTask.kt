package com.teamsixers.vmail.ui.compose.IdleState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeTask

/**
 * back to previous page
 */
class BackTask: ComposeTask() {

    /**
     * close this page
     * @param activity ComposeActivity
     */
    override fun doTask(activity: ComposeActivity) {
        activity.finish()
        Log.d("com.teamsixers.vmail.ui.compose.IdleState.SendEmailTask", "Sending email now...")
    }
}