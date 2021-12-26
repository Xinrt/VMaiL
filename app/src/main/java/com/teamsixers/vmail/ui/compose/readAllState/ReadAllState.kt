package com.teamsixers.vmail.ui.compose.readAllState

import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeState


/**
 * When executeCommand in ReadAllState() is called,
 * command will be wake up assistant.
 */
class ReadAllState : ComposeState() {

    /**
     * execute read all content
     * @param command wake up assistant.
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {

    }

    fun readAll(activity: ComposeActivity){
        // read content to composeActivity
        val holeContent = activity.binding.activityComposeMailContentEt.text.toString()
            activity.textToSpeech("your hole content is $holeContent")
        }

    /**
     * when text to speech start
     * @param activity ComposeActivity
     */
    override fun onTTSStart(activity: ComposeActivity) {
    }

    /**
     * when text to speech done
     * @param activity ComposeActivity
     */
    override fun onTTSDone(activity: ComposeActivity) {
    }

    /**
     * when text to speech occurs an error
     * @param activity ComposeActivity
     */
    override fun onTTSError(activity: ComposeActivity) {
    }

}
