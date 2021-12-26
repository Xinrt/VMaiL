package com.teamsixers.vmail.ui.compose.IdleState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeTask
import com.teamsixers.vmail.ui.compose.addContentState.AddContentState

/**
 * add content
 */
class AddContentTask: ComposeTask() {
    companion object {
        val TAG: String = AddContentTask::class.java.simpleName
    }

    /**
     * change activity state from IdleState to AddContentState
     * @param activity ComposeActivity
     */
    override fun doTask(activity: ComposeActivity) {
        // change activity state from IdleState to AddContentState
        activity.state = AddContentState()
        activity.textToSpeech("You can say content now")
        Log.d(TAG, "Adding content now...")
    }

}