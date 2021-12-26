package com.teamsixers.vmail.ui.compose.IdleState

import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeState

/**
 * idle state
 */
class IdleState: ComposeState() {
    /**
     * empty function
     * @param command input speech text
     * @param activity ComposeActivity
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {
        activity.textToSpeech("Sorry, I don't understand $command in IdleState. You can say the following command ${cmdTaskMap.keys}")
    }

    /**
     * empty function
     * @param activity ComposeActivity
     */
    override fun onTTSStart(activity: ComposeActivity) {
    }

    /**
     * empty function
     * @param activity ComposeActivity
     */
    override fun onTTSDone(activity: ComposeActivity) {
    }

    /**
     * empty function
     * @param activity ComposeActivity
     */
    override fun onTTSError(activity: ComposeActivity) {
    }
}