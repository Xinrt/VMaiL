package com.teamsixers.vmail.ui.compose.replaceState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeTask
import com.teamsixers.vmail.ui.compose.ReplaceSwitchState

/**
 * check tasks
 */
class CheckSequenceTask(private val order : Int, private val startIndexList : List<Int>, val command : String, private val findResultList: List<String>): ComposeTask() {

    companion object {
        val TAG: String = CheckSequenceTask::class.java.simpleName
    }

    /**
     * check the user voice to determine the index of word
     * @param activity ComposeActivity
     */
    override fun doTask(activity: ComposeActivity) {
        // check the user voice to determine the index of word
        activity.state = ReplaceWordState()
        if (startIndexList.size < order){
            activity.state = object : ReplaceSwitchState(startIndexList, command, findResultList){

                /**
                 * when users have no response, give response
                 * @param activity ComposeActivity
                 */
                override fun onUnknownResponse(activity: ComposeActivity) {
                    activity.textToSpeech("The number is too big, please say a number smaller")
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
                    Log.d("ReplaceWordState", "onDone replace word tts...")
                    activity.startActiveListening(activity)
                }

                /**
                 * when text to speech occurs an error
                 * @param activity ComposeActivity
                 */
                override fun onTTSError(activity: ComposeActivity) {
                }
            }
            activity.textToSpeech("There are only ${startIndexList.size} same words in the content, please say the number again")
            return
        }
        val content = activity.binding.activityComposeMailContentEt.text.toString()
        val contentBeforeKeyWord = content.substring(0, startIndexList[order - 1])
        val contentAfterKeyWord = content.substring(
                startIndexList[order - 1] + command.trim().length,
                content.length - 1
        ) + content[content.length - 1]
        activity.state = AddNewWordState(
                contentBeforeKeyWord,
                contentAfterKeyWord,
                command.trim(),
                findResultList,
                (order-1)
        )
        activity.textToSpeech("What is the new word?")
        Log.d(TAG, "check word index...")
    }
}