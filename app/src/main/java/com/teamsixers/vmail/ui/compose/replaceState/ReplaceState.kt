package com.teamsixers.vmail.ui.compose.replaceState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeState
import com.teamsixers.vmail.ui.compose.SwitchState

/**
 * When executeCommand in ReplaceState() is called,
 * command will be word want to delete, Yes, No, or wake up assistant.
 */

class ReplaceState : ComposeState() {

    /**
     * execute replace command
     * @param command word want to replace, Yes, No, or wake up assistant.
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {

        // replace to composeActivity
        activity.state = object : SwitchState() {
            /**
             * when users say "yes"
             * change state to ReplaceWordState
             */
            override fun onYesResponse(activity: ComposeActivity) {
                activity.state = ReplaceWordState()
                activity.state.executeCommand(command, activity)
            }

            /**
             * when users say "no"
             * change state to ReplaceState
             */
            override fun onNoResponse(activity: ComposeActivity) {
                activity.state = ReplaceState()
                activity.textToSpeech("Please say the word you want to replace again")
            }

            /**
             * when users have no response
             * give response
             */
            override fun onUnknownResponse(activity: ComposeActivity) {
                activity.textToSpeech("You can only say yes to confirm the word or no to say a new word")
            }

            /**
             * when text to speech start
             * @param activity ComposeActivity
             */
            override fun onTTSStart(activity: ComposeActivity) {
            }

            /**
             * when text to speech done, start activity listening
             * @param activity ComposeActivity
             */
            override fun onTTSDone(activity: ComposeActivity) {
                Log.d("ReplaceState", "onDone replace tts...")
                activity.startActiveListening(activity)
            }

            /**
             * when text to speech occurs an error
             * @param activity ComposeActivity
             */
            override fun onTTSError(activity: ComposeActivity) {
            }
        }
        activity.textToSpeech("Do you want to replace word $command")

        Log.d("ReplaceState", "onDone StartListeningReplaceListener")

    }

    /**
     * when text to speech start
     * @param activity ComposeActivity
     */
    override fun onTTSStart(activity: ComposeActivity) {
    }

    /**
     * when text to speech done, start activity listening
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