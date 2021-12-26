package com.teamsixers.vmail.ui.compose.addreceiverstate

import android.util.Log
import com.blankj.utilcode.util.RegexUtils
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeState
import com.teamsixers.vmail.ui.compose.IdleState.IdleState
import com.teamsixers.vmail.ui.compose.SwitchState
import com.teamsixers.vmail.utils.StringUtils


/**
 * When executeCommand in AddReceiverState() is called,
 * command can only be the receiver email address.
 */
class AddReceiverState : ComposeState() {

    /**
     * execute add receiver command
     * @param command email address only.
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {
        // remove space
        val actualCommand = command.trim()
        // convert to correct email address format
        val receiverAddress = StringUtils.processRawEmailAddress(actualCommand)
        if (RegexUtils.isEmail(receiverAddress)) {
            activity.setReceiver(StringUtils.changeENumToANum(receiverAddress))

            // change from IdleState to SwitchState
            Log.d("AddReceiverState", "onDone StartListeningNextReceiverListener")
            activity.state = object : SwitchState() {
                /**
                 * when users say "yes"
                 * change state to AddReceiverState and tell user that they can start speaking
                 */
                override fun onYesResponse(activity: ComposeActivity) {
                    activity.state = AddReceiverState()
                    activity.textToSpeech("You can add your receiver now.")
                }

                /**
                 * if users say "no"
                 * change state to idle state
                 */
                override fun onNoResponse(activity: ComposeActivity) {
                    activity.state = IdleState()
                    activity.textToSpeech("Activity now is idle.")
                }

                /**
                 * if users have no response
                 * change state to idle state
                 */
                override fun onUnknownResponse(activity: ComposeActivity) {
                    activity.state = IdleState()
                    activity.textToSpeech("Stop adding receiver now")
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
                    Log.d("AddReceiverState", "onDone next receiver tts...")
                    activity.startActiveListening(activity)
                }

                /**
                 * when text to speech occurs an error
                 * @param activity ComposeActivity
                 */
                override fun onTTSError(activity: ComposeActivity) {
                }
            }
            activity.textToSpeech("Newly added receiver is $receiverAddress. Do you have next receiver?")
        } else {
            activity.textToSpeech("You can only say email address of receiver now or say vmail to quit")
        }
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