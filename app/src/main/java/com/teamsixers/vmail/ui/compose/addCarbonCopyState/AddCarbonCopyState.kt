package com.teamsixers.vmail.ui.compose.addCarbonCopyState

import android.util.Log
import com.blankj.utilcode.util.RegexUtils
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeState
import com.teamsixers.vmail.ui.compose.IdleState.IdleState
import com.teamsixers.vmail.ui.compose.SwitchState
import com.teamsixers.vmail.utils.StringUtils


/**
 * When executeCommand in AddCarbonCopyState() is called,
 * command can only be the carbon copy email address.
 */
class AddCarbonCopyState : ComposeState() {

    /**
     * execute add carbon copy command
     * @param command email address only.
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {
        // remove space
        val actualCommand = command.trim()
        // convert to correct email address format
        val ccAddress = StringUtils.processRawEmailAddress(actualCommand)
        if (RegexUtils.isEmail(ccAddress)) {
            activity.setCarbonCopy(StringUtils.changeENumToANum(ccAddress))

            // change from IdleState to SwitchState
            Log.d("AddCarbonCopyState", "onDone StartListeningNextCarbonCopyListener")
            activity.state = object : SwitchState() {
                /**
                 * when users say "yes"
                 * change state to AddCarbonCopyState and tell user that they can start speaking
                 */
                override fun onYesResponse(activity: ComposeActivity) {
                    activity.state = AddCarbonCopyState()
                    activity.textToSpeech("You can add your carbon copy now.")
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
                    activity.textToSpeech("Stop adding carbon copy now")
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
                    Log.d("AddCarbonCopyState", "onDone next cc tts...")
                    activity.startActiveListening(activity)
                }

                /**
                 * when text to speech occurs an error
                 * @param activity ComposeActivity
                 */
                override fun onTTSError(activity: ComposeActivity) {
                }
            }
            activity.textToSpeech("Newly added carbon copy is $ccAddress. Do you have next carbon copy?")
        } else {
            activity.textToSpeech("You can only say email address of carbon copy now or say vmail to quit")
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