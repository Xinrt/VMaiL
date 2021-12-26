package com.teamsixers.vmail.ui.compose.IdleState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeTask

/**
 * stop adding content
 */
class StopContentTask: ComposeTask() {
    companion object {
        val TAG: String = StopContentTask::class.java.simpleName
    }
    /**
     * change activity state to IdleState
     * @param activity ComposeActivity
     */
    override fun doTask(activity: ComposeActivity) {
        // change activity state to IdleState
        activity.state = IdleState()
        activity.textToSpeech("Stop adding content now")
        Log.d(TAG, "stop adding content now...")
    }

}