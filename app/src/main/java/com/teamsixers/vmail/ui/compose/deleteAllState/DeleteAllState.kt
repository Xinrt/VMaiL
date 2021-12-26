package com.teamsixers.vmail.ui.compose.deleteAllState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeState
import com.teamsixers.vmail.ui.compose.IdleState.IdleState
import com.teamsixers.vmail.ui.compose.SwitchState


/**
 * When executeCommand in DeleteAllState() is called,
 * command will be delete all or wake up assistant.
 */

class DeleteAllState : ComposeState() {

    /**
     * execute delete command
     * @param command delete all, yes, no or wake up assistant.
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {
        deleteAll(activity)
    }

    /**
     * delete all content
     * @param activity ComposeActivity
     */
    fun deleteAll(activity: ComposeActivity) {

        // delete content to composeActivity
        activity.state = object : SwitchState() {
            /**
             * when users say "yes"
             * change state to DeleteAllState and tell users that the content is empty now
             */
            override fun onYesResponse(activity: ComposeActivity) {
                activity.state = DeleteAllState()
                activity.textToSpeech("Your content is empty now.")
            }

            /**
             * when users say "no"
             * change state to IdleState
             */
            override fun onNoResponse(activity: ComposeActivity) {
                activity.state = IdleState()
                activity.textToSpeech("Activity now is idle.")
            }

            /**
             * when users have no response
             * change state to IdleState
             */
            override fun onUnknownResponse(activity: ComposeActivity) {
                activity.state = IdleState()
                activity.textToSpeech("Stop deleting content now")
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
                Log.d("DeleteAllState", "onDone delete all content tts...")
                activity.startActiveListening(activity)
            }

            /**
             * when text to speech occurs an error
             * @param activity ComposeActivity
             */
            override fun onTTSError(activity: ComposeActivity) {
            }

        }



        Log.d("DeleteAllState", "onDone StartListeningDeleteAllContentListener")
        activity.textToSpeech("Your content is ${activity.binding.activityComposeMailContentEt.text.toString()}, Do you want to delete them all?")
    }


    /**
     * when text to speech start
     * @param activity ComposeActivity
     */
    override fun onTTSStart(activity: ComposeActivity) {
    }

    /**
     * when text to speech done, set current content empty
     * @param activity ComposeActivity
     */
    override fun onTTSDone(activity: ComposeActivity) {
        activity.binding.activityComposeMailContentEt.setText("")
    }

    /**
     * when text to speech occurs an error
     * @param activity ComposeActivity
     */
    override fun onTTSError(activity: ComposeActivity) {
    }

}