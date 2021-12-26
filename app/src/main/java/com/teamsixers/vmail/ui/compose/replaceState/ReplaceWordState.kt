package com.teamsixers.vmail.ui.compose.replaceState

import android.util.Log
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeState
import com.teamsixers.vmail.ui.compose.IdleState.IdleState
import com.teamsixers.vmail.ui.compose.ReplaceSwitchState

/**
 * When executeCommand in ReplaceWordState() is called,
 * command will be the serial number or wake up assistant.
 */

class ReplaceWordState : ComposeState() {
    private val orderMap = hashMapOf(
            0 to "first",
            1 to "second",
            2 to "third",
            3 to "fourth",
            4 to "fifth",
            5 to "sixth",
            6 to "seventh",
            7 to "eighth",
            8 to "ninth"
    )
    /**
     * execute replace word command
     * @param command serial number or wake up assistant.
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {
        val content = activity.binding.activityComposeMailContentEt.text.toString()
        // replace word to composeActivity
        if (content.contains(command.trim(), true)){
            val newCommand = command.trim()

            val (startIndexList, findResultList) = getWordIndex(newCommand, content)

            val sb = StringBuilder(10)
            val list: MutableList<String> = getSentence(activity, startIndexList, command.trim())

            var order = ""
            for (i in 0 until (startIndexList.size)) {
                order = orderMap[i].toString()
                sb.append("The $order occurrence of ${command.trim()} is the sentence ${list[i]}")
            }


            activity.state = object : ReplaceSwitchState(startIndexList, command, findResultList){

                /**
                 * when users have no response
                 * give response
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
            activity.textToSpeech("There are ${startIndexList.size} same words in the content, $sb, " +
                    "which one would you want to replace, you can only say sequence number.")
        } else {
            activity.state = IdleState()
            activity.textToSpeech("Sorry, the content doesn't have that word")
        }

        Log.d("ReplaceWordState", "onDone StartListeningReplaceWordListener")

    }

    /**
     * get index of word
     * @param newCommand text of new command
     * @param content content of email
     * @return pair of start index and find index
     */
    private fun getWordIndex(newCommand: String, content: String): Pair<List<Int>, List<String>> {
        val regex = """(?i)\b$newCommand\b""".toRegex()
        val matchResult = regex.findAll(content)

        val startIndexList = matchResult.map { result ->
            result.range.first
        }.toList()

        val findResultList = matchResult.map { result ->
            result.value
        }.toList()

        return Pair(startIndexList, findResultList)
    }

    /**
     * get the goal sentence
     * @param activity ComposeActivity
     * @param startIndexList a list of start index
     * @param command a string of command
     * @return a mutable list
     */
    private fun getSentence(activity: ComposeActivity, startIndexList: List<Int>, command: String): MutableList<String> {
        val list: MutableList<String> = mutableListOf()
        val content = activity.binding.activityComposeMailContentEt.text.toString()
        var contentAfterKeyWord = ""
        var lastDotIndex = 0
        var newSentenceBefore = ""
        var holeSentence = ""

        for (j in 0 until (startIndexList.size)) {
            lastDotIndex = content.substring(0, startIndexList[j]).lastIndexOf(".")
            contentAfterKeyWord = content.substring(
                    startIndexList[j] + command.trim().length,
                    content.length - 1
            ) + content[content.length - 1]
            if (lastDotIndex != -1) {
                newSentenceBefore = content.substring(0, startIndexList[j]).substring(lastDotIndex + 1,
                        content.substring(0, startIndexList[j]).length - 1) +
                        content.substring(0, startIndexList[j])[content.substring(0, startIndexList[j]).length - 1]

                holeSentence = newSentenceBefore + command + contentAfterKeyWord.substring(0, contentAfterKeyWord.indexOf("."))
            } else {
                holeSentence = command + contentAfterKeyWord.substring(0, contentAfterKeyWord.indexOf("."))
            }
            list.add(holeSentence)
        }
        return list
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

