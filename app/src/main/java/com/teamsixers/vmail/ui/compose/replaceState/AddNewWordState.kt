package com.teamsixers.vmail.ui.compose.replaceState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeState
import com.teamsixers.vmail.ui.compose.IdleState.IdleState
import com.teamsixers.vmail.utils.StringUtils
import java.util.*

/**
 * When executeCommand in AddNewContent() is called,
 * command will be the new word want to write or wake up assistant.
 */

class AddNewWordState(before : String, after : String, past : String, list: List<*>, index : Int) : ComposeState() {

    var pastWord = ""
    var contentBeforeKeyWord = ""
    var contentAfterKeyWord = ""
    var listForCheck: List<*>
    var indexOfWord = 0

    init{
        pastWord = past
        contentBeforeKeyWord = before
        contentAfterKeyWord = after
        listForCheck = list
        indexOfWord = index
    }

    /**
     * execute add new word command
     * @param command the new word want to write or wake up assistant.
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {
        // add new word to composeActivity
        val word = command.toLowerCase(Locale.ROOT).trim()
        val pastChar = word[0].toString()
        val newChar = pastChar.toUpperCase(Locale.ROOT)
        val remainWord = StringUtils.getStringAfter(command,newChar)
        var newWord = ""

        newWord = if (listForCheck[indexOfWord] == pastWord){
            newChar+remainWord
        } else {
            word
        }

        val content = contentBeforeKeyWord+newWord+contentAfterKeyWord
        activity.binding.activityComposeMailContentEt.setText(content)

        activity.state = IdleState()
        activity.textToSpeech("Replace $pastWord with $newWord successfully")

        Log.d("DeleteWordState", "onDone StartListeningDeleteWordListener")

    }

    /**
     * when text to speech start
     * @param activity ComposeActivity
     */
    override fun onTTSStart(activity: ComposeActivity) {
    }

    /**
     * when text to speech done, start active listening
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