package com.teamsixers.vmail.ui.compose.IdleState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeTask
import com.teamsixers.vmail.ui.compose.addBlindCarbonCopyState.AddBlindCarbonCopyState

/**
 * add blind carbon copy
 */
class AddBlindCarbonCopyTask: ComposeTask() {
    companion object {
        val TAG: String = AddBlindCarbonCopyTask::class.java.simpleName
    }
    /**
     * change activity state from IdleState to AddBlindCarbonCopyState
     * @param activity ComposeActivity
     */
    override fun doTask(activity: ComposeActivity) {
        // change activity state from IdleState to AddBlindCarbonCopyState
        activity.state = AddBlindCarbonCopyState()
        activity.textToSpeech("You can say blind carbon copy email address now.")
        Log.d(TAG, "Adding blind carbon copy now...")
    }
}