package com.teamsixers.vmail.ui.compose.IdleState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeTask
import com.teamsixers.vmail.ui.compose.replaceState.ReplaceState

/**
 * replace
 */
class ReplaceTask: ComposeTask() {
    companion object {
        val TAG: String = ReplaceTask::class.java.simpleName
    }

    /**
     * change activity state from IdleState to ReplaceState
     * @param activity ComposeActivity
     */
    override fun doTask(activity: ComposeActivity) {
        // change activity state from IdleState to ReplaceState
        activity.state = ReplaceState()
        activity.textToSpeech("which word you want to replace")
        Log.d(TAG, "replace word now...")
    }

}