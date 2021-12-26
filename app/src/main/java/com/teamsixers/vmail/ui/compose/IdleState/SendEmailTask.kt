package com.teamsixers.vmail.ui.compose.IdleState

import android.app.Activity
import android.util.Log
import com.teamsixers.vmail.ui.Task
import com.teamsixers.vmail.ui.compose.ComposeActivity

/**
 * send email
 */
class SendEmailTask: Task() {
    /**
     * SendEmailTask will perform the email sending task in compose activity context
     * @param activity: Activity should be class or subclass of ComposeActivity
     */
    override fun doTask(activity: Activity) {
        if (activity is ComposeActivity) {
            activity.checkForm()
            Log.d("com.teamsixers.vmail.ui.compose.IdleState.SendEmailTask", "Sending email now...")
            return
        }
        throw RuntimeException("Send email should only perform in composeActivity.")
    }
}