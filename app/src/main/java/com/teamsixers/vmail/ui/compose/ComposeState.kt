package com.teamsixers.vmail.ui.compose

import android.app.Activity
import com.teamsixers.vmail.ui.State
import com.teamsixers.vmail.ui.Task
import com.teamsixers.vmail.ui.compose.IdleState.*
import me.xdrop.fuzzywuzzy.FuzzySearch

/**
 * compose state
 */
abstract class ComposeState: State() {
    /**
     * Key: string is the command.
     * Value: Task is the class encapsulating the task of the command.
     */
    open val cmdTaskMap by lazy {
        hashMapOf<String, Task>(
                "show all commands" to ShowAllCommandTask(),
                "send message" to SendEmailTask(),
                "back" to BackTask(),
                "add carbon copy" to AddCarbonCopyTask(),
                "add blind carbon copy" to AddBlindCarbonCopyTask(),
                "add a receiver" to AddReceiverTask(),
                "add subject" to AddSubjectTask(),
                "add content" to AddContentTask(),
                "delete word" to DeleteWordTask(),
                "delete sentence" to DeleteSentenceTask(),
                "read all" to ReadAllTask(),
                "delete all" to DeleteAllTask(),
                "replace word" to ReplaceTask(),
                "stop compose" to StopContentTask()
        )
    }
    abstract fun executeCommand(command: String, activity: ComposeActivity)
    abstract fun onTTSStart(activity: ComposeActivity)
    abstract fun onTTSDone(activity: ComposeActivity)
    abstract fun onTTSError(activity: ComposeActivity)

    /**
     * when text to speech start
     * if current activity is ComposeActivity
     * start corresponding functions
     *
     * @param activity an activity in this APP
     */
    override fun onTTSStart(activity: Activity) {
        if (activity is ComposeActivity) {
            onTTSStart(activity)
            return
        }
        throw RuntimeException("Compose state should only perform in composeActivity.")

    }

    /**
     * when text to speech done
     * if current activity is ComposeActivity
     * start corresponding functions
     *
     * @param activity an activity in this APP
     */
    override fun onTTSDone(activity: Activity) {
        if (activity is ComposeActivity) {
            onTTSDone(activity)
            return
        }
        throw RuntimeException("Compose state should only perform in composeActivity.")
    }

    /**
     * when text to speech occurrs an error
     * if current activity is ComposeActivity
     * start corresponding functions
     *
     * @param activity an activity in this APP
     */
    override fun onTTSError(activity: Activity) {
        if (activity is ComposeActivity) {
            onTTSError(activity)
            return
        }
        throw RuntimeException("Compose state should only perform in composeActivity.")
    }



    /**
     * Execute specific task given by the command.
     * If no command is matched, notify caller that no command matched.
     *
     * @since 2.0
     *
     * @author Mingyan(Cyril) Wang
     */
    override fun executeCommand(command: String, activity: Activity) {


        if (activity is ComposeActivity) {
            val findResult = FuzzySearch.extractOne(command, cmdTaskMap.keys)
            if (findResult.score > 95) {
                val task = cmdTaskMap[findResult.string]
                if (task != null) {
                    task.doTask(activity)
                    return
                }
                throw RuntimeException("Task is not defined matching ${findResult.string}")
            } else {
                executeCommand(command, activity)
                return
            }
        }
        throw RuntimeException("Compose state should only perform in composeActivity.")
    }


}