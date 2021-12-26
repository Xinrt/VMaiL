package com.teamsixers.vmail.ui.compose.IdleState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeTask
import com.teamsixers.vmail.ui.compose.deleteWordState.DeleteWordState

/**
 * delete word
 */
class DeleteWordTask: ComposeTask() {
        companion object {
            val TAG: String = DeleteWordTask::class.java.simpleName
        }
    /**
     * change activity state from IdleState to DeleteWordState
     * @param activity ComposeActivity
     */
        override fun doTask(activity: ComposeActivity) {
            // change activity state from IdleState to DeleteWordState

            val content = activity.binding.activityComposeMailContentEt.text.toString()
            if (content != ""){
                activity.state = DeleteWordState()
                activity.textToSpeech("How many words you want to delete?")
                Log.d(TAG, "Delete word now...")
            } else {
                activity.state = IdleState()
                activity.textToSpeech("You have no content now, can not delete anything")
            }

        }

    }