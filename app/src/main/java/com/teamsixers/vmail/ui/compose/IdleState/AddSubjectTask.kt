package com.teamsixers.vmail.ui.compose.IdleState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeTask
import com.teamsixers.vmail.ui.compose.addSubjectState.AddSubjectState

/**
 * add subject
 */
class AddSubjectTask: ComposeTask() {
    companion object {
        val TAG: String = AddSubjectTask::class.java.simpleName
    }

    /**
     * change activity state from IdleState to AddSubjectState
     * @param activity ComposeActivity
     */
    override fun doTask(activity: ComposeActivity) {
        // change activity state from IdleState to AddSubjectState
        activity.state = AddSubjectState()
        activity.textToSpeech("You can say subject now")
        Log.d(TAG, "Adding subject now...")
    }

}