package com.teamsixers.vmail.ui.compose

import com.teamsixers.vmail.ui.Task
import com.teamsixers.vmail.ui.compose.replaceState.CheckSequenceTask
import me.xdrop.fuzzywuzzy.FuzzySearch

/**
 * switch-state of replace function
 */
abstract class ReplaceSwitchState(private val startIndexList: List<Int>, val command: String, private val findResultList: List<String>) : ComposeState() {

    val sequenceTaskMap by lazy {
        hashMapOf<String, Task>(
            "first" to CheckSequenceTask(1, startIndexList, command, findResultList),
            "second" to CheckSequenceTask(2, startIndexList, command, findResultList),
            "third" to CheckSequenceTask(3, startIndexList, command, findResultList),
            "fourth" to CheckSequenceTask(4, startIndexList, command, findResultList),
            "fifth" to CheckSequenceTask(5, startIndexList, command, findResultList),
            "sixth" to CheckSequenceTask(6, startIndexList, command, findResultList),
            "seventh" to CheckSequenceTask(7, startIndexList, command, findResultList),
            "eighth" to CheckSequenceTask(8, startIndexList, command, findResultList),
            "ninth" to CheckSequenceTask(9, startIndexList, command, findResultList)
        )
    }

    /**
     * if input speech matches default text over 85%
     * take it as a valid input
     *
     * @param command input speech text
     * @param activity ComposeActivity
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {

        val findResult = FuzzySearch.extractOne(command.trim(), sequenceTaskMap.keys)
        if (findResult.score > 85) {
            val task = sequenceTaskMap[findResult.string]
            if (task != null) {
                task.doTask(activity)
                return
            }
            throw RuntimeException("Task is not defined matching ${findResult.string}")
        } else {
            activity.textToSpeech("You can only say sequence number now.")
            return
        }
    }

    /**
     * when the response of user cannot be recognized
     * @param activity ComposeActivity
     */
    abstract fun onUnknownResponse(activity: ComposeActivity)

}