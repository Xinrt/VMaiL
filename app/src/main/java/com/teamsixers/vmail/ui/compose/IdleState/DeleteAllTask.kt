package com.teamsixers.vmail.ui.compose.IdleState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeTask
import com.teamsixers.vmail.ui.compose.deleteAllState.DeleteAllState

/**
 * delete all
 */
class DeleteAllTask: ComposeTask() {
    companion object {
        val TAG: String = DeleteAllTask::class.java.simpleName
    }

    /**
     * change activity state from IdleState to DeleteAllState
     * @param activity ComposeActivity
     */
    override fun doTask(activity: ComposeActivity) {
        // change activity state from IdleState to DeleteAllState
        val content = activity.binding.activityComposeMailContentEt.text.toString()
        if (content != ""){
            val deleteAllState = DeleteAllState()
            activity.state = deleteAllState
            deleteAllState.deleteAll(activity)
            Log.d(TAG, "Delete all now...")
        } else {
            activity.state = IdleState()
            activity.textToSpeech("You have no content now, can not delete anything")
        }

    }

}