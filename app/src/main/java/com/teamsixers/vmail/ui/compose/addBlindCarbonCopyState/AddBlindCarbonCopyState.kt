package com.teamsixers.vmail.ui.compose.addBlindCarbonCopyState

import android.util.Log
import com.blankj.utilcode.util.RegexUtils
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeState
import com.teamsixers.vmail.ui.compose.IdleState.IdleState
import com.teamsixers.vmail.ui.compose.SwitchState
import com.teamsixers.vmail.utils.StringUtils


/**
 * When executeCommand in AddBlindCarbonCopyState() is called,
 * command can only be the blind carbon copy email address.
 */
class AddBlindCarbonCopyState : ComposeState() {

    /**
     * execute add blind carbon copy command
     * @param command email address only.
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {
        // remove space
        val actualCommand = command.trim()
        // convert to correct email address format
        val bccAddress = StringUtils.processRawEmailAddress(actualCommand)
        if (RegexUtils.isEmail(bccAddress)) {
            activity.setBlindCarbonCopy(StringUtils.changeENumToANum(bccAddress))

            // change from IdleState to SwitchState
            Log.d("AddBlindCarbonCopyState", "onDone StartListeningNextBlindCarbonCopyListener")
            activity.state = object : SwitchState() {
                /**
                 * when users say "yes"
                 * change state to AddBlindCarbonCopyState and tell user that they can start speaking
                 */
                override fun onYesResponse(activity: ComposeActivity) {
                    activity.state = AddBlindCarbonCopyState()
                    activity.textToSpeech("You can add your blind carbon copy now.")
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
                    activity.textToSpeech("Stop adding blind carbon copy now")
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
                    Log.d("AddBlindCarbonCopyState", "onDone next bcc tts...")
                    activity.startActiveListening(activity)
                }

                /**
                 * when text to speech occurs an error
                 * @param activity ComposeActivity
                 */
                override fun onTTSError(activity: ComposeActivity) {
                }
            }
            activity.textToSpeech("Newly added blind carbon copy is $bccAddress. Do you have next blind carbon copy?")
        } else {
            activity.textToSpeech("You can only say email address of blind carbon copy now or say vmail to quit")
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