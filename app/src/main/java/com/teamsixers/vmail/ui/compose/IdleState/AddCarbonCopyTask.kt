package com.teamsixers.vmail.ui.compose.IdleState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeTask
import com.teamsixers.vmail.ui.compose.addCarbonCopyState.AddCarbonCopyState

/**
 * add carbon copy
 */
class AddCarbonCopyTask: ComposeTask() {
    companion object {
        val TAG: String = AddCarbonCopyTask::class.java.simpleName
    }
    /**
     * change activity state from IdleState to AddCarbonCopyState
     * @param activity ComposeActivity
     */
    override fun doTask(activity: ComposeActivity) {
        // change activity state from IdleState to AddCarbonCopyState
        activity.state = AddCarbonCopyState()
        activity.textToSpeech("You can say carbon copy email address now.")
        Log.d(TAG, "Adding carbon copy now...")
    }
}