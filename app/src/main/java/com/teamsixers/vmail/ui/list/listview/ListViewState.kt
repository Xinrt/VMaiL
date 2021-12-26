package com.teamsixers.vmail.ui.list.listview

import android.app.Activity
import com.teamsixers.vmail.ui.State
import com.teamsixers.vmail.ui.Task
import com.teamsixers.vmail.ui.list.listview.idlestate.*
import me.xdrop.fuzzywuzzy.FuzzySearch

/**
 * ListViewState provides mapping for commands and tasks.
 */
abstract class ListViewState: State() {
    /**
     * Key: string is the command.
     * Value: Task is the class encapsulating the task of the command.
     */
    open val cmdTaskMap by lazy {
        hashMapOf<String, Task>(
            "read unread message" to ReadUnreadMessageTask(),
            "show all commands" to ShowAllCommandTask(),
            "read message" to ReadMessageTask(),
            "compose message" to ComposeEmailTask(),
            "back" to BackTask()
        )
    }
    abstract fun executeCommand(command: String, activity: ListActivity)
    abstract fun onTTSStart(activity: ListActivity)
    abstract fun onTTSDone(activity: ListActivity)
    abstract fun onTTSError(activity: ListActivity)

    /**
     * Command should be one of the key in the map
     * If match result is greater than 85, it will do the correspondent task
     * @param command the command of user(Recognition result of user voice input).
     * @param activity the activity to perform the task for command.
     */
    override fun executeCommand(command: String, activity: Activity) {
        if (activity is ListActivity) {
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
        throw RuntimeException("ListViewState should only perform in listActivity.")
    }

    override fun onTTSStart(activity: Activity) {
        if (activity is ListActivity) {
            onTTSStart(activity)
            return
        }
        throw RuntimeException("ListViewState should only perform in listActivity.")
    }

    override fun onTTSDone(activity: Activity) {
        if (activity is ListActivity) {
            onTTSDone(activity)
            return
        }
        throw RuntimeException("ListViewState should only perform in listActivity.")
    }

    override fun onTTSError(activity: Activity) {
        if (activity is ListActivity) {
            onTTSError(activity)
            return
        }
        throw RuntimeException("ListViewState should only perform in listActivity.")
    }
}