package com.teamsixers.vmail.ui.login

import android.app.Activity
import com.teamsixers.vmail.ui.State

/**
 * See BaseActivity explanation for IdleState.
 */
class IdleState: State() {
    override fun executeCommand(command: String, activity: Activity) {}
    override fun onTTSStart(activity: Activity) {}
    override fun onTTSDone(activity: Activity) {}
    override fun onTTSError(activity: Activity) {}
}