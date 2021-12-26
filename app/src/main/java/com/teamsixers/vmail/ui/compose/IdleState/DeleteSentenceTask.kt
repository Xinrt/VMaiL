package com.teamsixers.vmail.ui.compose.IdleState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeTask
import com.teamsixers.vmail.ui.compose.deleteSentenceState.DeleteSentenceState

/**
 * delete sentence
 */
class DeleteSentenceTask: ComposeTask() {
    companion object {
        val TAG: String = DeleteSentenceTask::class.java.simpleName
    }

    /**
     * change activity state from IdleState to DeleteSentenceState
     * @param activity ComposeActivity
     */
    override fun doTask(activity: ComposeActivity) {
        // change activity state from IdleState to DeleteSentenceState

        val content = activity.binding.activityComposeMailContentEt.text.toString()
        if (content != ""){
            activity.state = DeleteSentenceState()
            activity.textToSpeech("How many sentences you want to delete?")
            Log.d(TAG, "Delete sentence now...")
        } else {
            activity.state = IdleState()
            activity.textToSpeech("You have no content now, can not delete anything")
        }

    }


}