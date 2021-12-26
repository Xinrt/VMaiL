package com.teamsixers.vmail.ui.detail

import android.app.Activity
import com.teamsixers.vmail.ui.State
import com.teamsixers.vmail.ui.Task
import com.teamsixers.vmail.ui.detail.idlestate.*
import me.xdrop.fuzzywuzzy.FuzzySearch

/**
 * detail activity state
 */
abstract class DetailActivityState: State() {
    companion object {
        /**
         * Key: string is the command.
         * Value: Task is the class encapsulating the task of the command.
         */
        val cmdTaskMap by lazy {
            hashMapOf<String, Task>(
                "back" to BackTask(),
                "read content" to ReadContentTask(),
                "what is the subject" to ReadSubjectTask(),
                "who is the sender" to ReadSenderTask(),
                "show all commands" to ShowAllCommandTask()
            )
        }

    }
    abstract fun executeCommand(command: String, activity: DetailActivity)
    abstract fun onTTSStart(activity: DetailActivity)
    abstract fun onTTSDone(activity: DetailActivity)
    abstract fun onTTSError(activity: DetailActivity)

    /**
     * if input speech matches default text over 85%
     * take it as a valid input
     *
     * @param command input speech text
     * @param activity Activity should be class or subclass of DetailActivity
     */
    override fun executeCommand(command: String, activity: Activity) {
        if (activity is DetailActivity) {
            val findResult = FuzzySearch.extractOne(command, cmdTaskMap.keys)
            if (findResult.score > 85) {
                val task = cmdTaskMap[findResult.string]
                if (task != null) {
                    task.doTask(activity)
                    return
                } else {
                    executeCommand(command, activity)
                }
                throw RuntimeException("Task is not defined matching ${findResult.string}")
            } else {
                executeCommand(command, activity)
            }
            return
        }
        throw RuntimeException("Detail activity should only perform in detailActivity.")
    }

    /**
     * when text to speech start
     * if current activity is DetailActivity
     * start corresponding functions
     *
     * @param activity Activity should be class or subclass of DetailActivity
     */
    override fun onTTSStart(activity: Activity) {
        if (activity is DetailActivity) {
            onTTSStart(activity)
            return
        }
        throw RuntimeException("Detail ViewState should only perform in listActivity.")
    }

    /**
     * when text to speech done
     * if current activity is DetailActivity
     * start corresponding functions
     * @param activity Activity should be class or subclass of DetailActivity
     *
     */
    override fun onTTSDone(activity: Activity) {
        if (activity is DetailActivity) {
            onTTSDone(activity)
            return
        }
        throw RuntimeException("Detail ViewState should only perform in listActivity.")
    }

    /**
     * when text to speech occurs an error
     * if current activity is DetailActivity
     * start corresponding functions
     * @param activity Activity should be class or subclass of DetailActivity
     */
    override fun onTTSError(activity: Activity) {
        if (activity is DetailActivity) {
            onTTSError(activity)
            return
        }
        throw RuntimeException("Detail ViewState should only perform in listActivity.")
    }
}