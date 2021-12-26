package com.teamsixers.vmail.ui.compose.IdleState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeTask
import com.teamsixers.vmail.ui.compose.addreceiverstate.AddReceiverState

/**
 * add receiver
 */
class AddReceiverTask: ComposeTask() {
    companion object {
        val TAG: String = AddReceiverTask::class.java.simpleName
    }

    /**
     * change activity state from IdleState to AddReceiverState
     * @param activity ComposeActivity
     */
    override fun doTask(activity: ComposeActivity) {
        // change activity state from IdleState to AddReceiverState
        activity.state = AddReceiverState()
        activity.textToSpeech("You can say receiver email address now")
        Log.d(TAG, "Adding receiver now...")
    }

}