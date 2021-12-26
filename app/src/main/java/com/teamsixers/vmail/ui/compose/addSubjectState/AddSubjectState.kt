package com.teamsixers.vmail.ui.compose.addSubjectState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeState

/**
 * When executeCommand in AddSubject() is called,
 * command will be subject or wake up assistant.
 */
class AddSubjectState : ComposeState() {

    /**
     * execute add subject command
     * @param command subject or wake up assistant.
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {

        // set content to composeActivity
        activity.setSubject(command)

        activity.state = activity.idleState
        Log.d("AddSubjectState", "onDone StartListeningSubjectListener")
        activity.textToSpeech("The new subject is $command")
    }

    /**
     * when text to speech start
     * @param activity ComposeActivity
     */
    override fun onTTSStart(activity: ComposeActivity) {
    }

    /**
     * when text to speech done
     * start active listening
     * @param activity ComposeActivity
     */
    override fun onTTSDone(activity: ComposeActivity) {
        activity.startActiveListening(activity)
    }


    /**
     * when text to speech occurs an error
     * @param activity ComposeActivity
     */
    override fun onTTSError(activity: ComposeActivity) {
    }

}