package com.teamsixers.vmail.ui.compose.addContentState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeState

/**
 * When executeCommand in AddContentState() is called,
 * command will be content or wake up assistant.
 */
class AddContentState : ComposeState() {

    /**
     * execute add content command
     * @param command content or wake up assistant.
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {

        // set content to composeActivity
        activity.setContent(command)

            Log.d("AddContentState", "onDone StartListeningContentListener")
            activity.textToSpeech("The new content is $command, you can see next content now")
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