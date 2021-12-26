package com.teamsixers.vmail.ui.compose.deleteWordState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeState
import com.teamsixers.vmail.ui.compose.IdleState.IdleState
import com.teamsixers.vmail.ui.compose.SwitchState
import com.teamsixers.vmail.ui.compose.deleteAllState.DeleteAllState
import com.teamsixers.vmail.utils.StringUtils
import me.xdrop.fuzzywuzzy.FuzzySearch


/**
 * When executeCommand in DeleteWordState() is called,
 * command will be number of delete words, Yes, No, or wake up assistant.
 */

class DeleteWordState : ComposeState() {

    /**
     * execute delete words command
     * @param command number of delete words, Yes, No, or wake up assistant.
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {
        val sequenceList = listOf("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine")
        val matchedResult = FuzzySearch.extractOne(command, sequenceList)

        // delete word to composeActivity
        if (sequenceList.contains(command.trim())){
            val numOfDeleteWords = StringUtils.changeENumToANum(matchedResult.string).trim().toInt()
            val holeContent = activity.binding.activityComposeMailContentEt.text.toString()
            val countOfWord = spaceCount(holeContent) + 1

            if (countOfWord <= numOfDeleteWords){
                activity.state = object : SwitchState() {
                    /**
                     * when users say "yes"
                     * change state to DeleteAllState
                     */
                    override fun onYesResponse(activity: ComposeActivity) {
                        val deleteAllState = DeleteAllState()
                        activity.state = deleteAllState
                        deleteAllState.deleteAll(activity)
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
                        activity.textToSpeech("Stop deleting word now")
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
                        Log.d("DeleteWordState", "onDone delete word tts...")
                        activity.startActiveListening(activity)
                    }

                    /**
                     * when text to speech occurs an error
                     * @param activity ComposeActivity
                     */
                    override fun onTTSError(activity: ComposeActivity) {
                    }
                }
                activity.textToSpeech("your hole content word is less than the word you want to delete, do you want to delete them all?")
            } else {
                deleteSpecificWord(activity, numOfDeleteWords)
            }
        } else {
            activity.textToSpeech("please say number only")
        }



        Log.d("DeleteWordState", "onDone StartListeningDeleteWordListener")

    }

    /**
     * delete specific word
     * @param activity ComposeActivity
     * @param numOfDeleteWords number of words to delete
     */
    private fun deleteSpecificWord(activity: ComposeActivity, numOfDeleteWords: Int) {
        var numOfDeleteWordsCount = numOfDeleteWords
        var pastSentenceForDW = activity.binding.activityComposeMailContentEt.text.toString()
        var newSentenceIndexForDW = 0
        var newSentenceForDW = ""
        while (numOfDeleteWordsCount != 0) {
            newSentenceIndexForDW = pastSentenceForDW.lastIndexOf(" ")
            newSentenceForDW = "${pastSentenceForDW.substring(0, newSentenceIndexForDW)}."
            pastSentenceForDW = newSentenceForDW
            numOfDeleteWordsCount--
        }
        if (newSentenceForDW.contains("..")) {
            val indexOfFinalDot = newSentenceForDW.lastIndexOf("..") + 1
            newSentenceForDW = newSentenceForDW.substring(0, indexOfFinalDot)
        }
        activity.binding.activityComposeMailContentEt.setText(newSentenceForDW)
        activity.state = IdleState()
        activity.textToSpeech("The new content is $newSentenceForDW")
    }

    /**
     * count number of spaces
     * @param str input sentence
     * @return number of spaces
     */
    private fun spaceCount(str: String) : Int{
        var count = 0
        var start = 0
        while (str.indexOf(" ", start) >= 0 && start < str.length) {
            count++
            start = str.indexOf(" ", start) + " ".length
        }
        return count
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