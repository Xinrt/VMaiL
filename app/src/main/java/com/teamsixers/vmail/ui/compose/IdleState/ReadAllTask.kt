package com.teamsixers.vmail.ui.compose.IdleState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeTask
import com.teamsixers.vmail.ui.compose.readAllState.ReadAllState

/**
 * read whole content
 */
class ReadAllTask: ComposeTask() {
    companion object {
        val TAG: String = ReadAllTask::class.java.simpleName
    }

    /**
     * change activity state from IdleState to ReadAllState
     * @param activity ComposeActivity
     */
    override fun doTask(activity: ComposeActivity) {
        // change activity state from IdleState to ReadAllState
        val readAllState = ReadAllState()
        activity.state = readAllState
        readAllState.readAll(activity)
        Log.d(TAG, "Read all content now...")
    }

}