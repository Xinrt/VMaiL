package com.teamsixers.vmail.ui.compose.deleteSentenceState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeState
import com.teamsixers.vmail.ui.compose.IdleState.IdleState
import com.teamsixers.vmail.ui.compose.SwitchState
import com.teamsixers.vmail.ui.compose.deleteAllState.DeleteAllState
import com.teamsixers.vmail.utils.StringUtils

/**
 * When executeCommand in DeleteSentenceState() is called,
 * command will be number of delete sentences, Yes, No, or wake up assistant.
 */
class DeleteSentenceState : ComposeState() {

    /**
     * execute delete sentences command
     * @param command number of delete sentences, Yes, No, or wake up assistant.
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {

        // delete sentence to composeActivity

        val sequenceList = listOf("One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine")

        if (sequenceList.contains(command.trim())){
            val numOfDeleteSentences = StringUtils.changeENumToANum(command).trim().toInt()
            val wholeContent = activity.binding.activityComposeMailContentEt.text.toString()
            val countOfSentence = getSentence(wholeContent).size
            if (countOfSentence <= numOfDeleteSentences){
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
                        activity.textToSpeech("Stop deleting sentence now")
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
                        Log.d("DeleteSentenceState", "onDone delete sentence tts...")
                        activity.startActiveListening(activity)
                    }

                    /**
                     * when text to speech occurs an error
                     * @param activity ComposeActivity
                     */
                    override fun onTTSError(activity: ComposeActivity) {
                    }
                }
                activity.textToSpeech("your whole content sentence is less than the sentence you want to delete, do you want to delete them all?")
            } else {
                deleteSpecificSentence(activity, numOfDeleteSentences)
            }
        } else {
            activity.textToSpeech("please say number only")
        }

        Log.d("DeleteWordState", "onDone StartListeningDeleteSentenceListener")

    }

    /**
     * delete specific sentence
     * @param activity ComposeActivity
     * @param numOfDeleteSentences number of sentences to delete
     */
    private fun deleteSpecificSentence(activity: ComposeActivity, numOfDeleteSentences: Int) {
        val numOfDeleteSentencesCount = numOfDeleteSentences
        val pastSentenceForDS = activity.binding.activityComposeMailContentEt.text.toString()
        val newSentenceForDS = getTextOfList(numOfDeleteSentencesCount,pastSentenceForDS)

        activity.binding.activityComposeMailContentEt.setText(newSentenceForDS)
        activity.state = IdleState()
        activity.textToSpeech("The new content is $newSentenceForDS")
    }


    /**
     * get numbered sentences
     * @param order order of a sentence
     * @param content content of a sentence
     * @return new content
     */
    private fun getTextOfList(order : Int, content: String) : String{
        var newContent = ""
        var remainCount = getSentence(content).size - order
        var i = 0
        while (remainCount != 0){
            newContent += getSentence(content)[i]
            i++
            remainCount--
        }
        return newContent
    }


    /**
     * get specific sentence
     * @param content input sentence
     * @return list of sentences
     */
    private fun getSentence(content: String): List<String> {
        val regex = """[^.!?\s][^.!?]*(?:[.!?](?!['"]?\s|$)[^.!?]*)*[.!?]?['"]?(?=\s|$)""".toRegex()
        val matchResult = regex.findAll(content)

        return matchResult.map { result ->
            result.value
        }.toList()
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