package com.teamsixers.vmail.ui.main

import android.app.Activity
import com.teamsixers.vmail.ui.State
import com.teamsixers.vmail.ui.Task
import com.teamsixers.vmail.ui.main.idlestate.BackTask
import me.xdrop.fuzzywuzzy.FuzzySearch

/**
 * MainViewState provides mapping for commands and tasks.
 */
abstract class MainViewState: State() {
    /**
     * Key: string is the command.
     * Value: Task is the class encapsulating the task of the command.
     */
    open val cmdTaskMap by lazy {
        hashMapOf<String, Task>(
                "read folder list" to ReadFolderListTask(),
                "read list" to ReadFolderListTask(),
                "open folder" to OpenFolderTask(),
                "show all commands" to ShowAllCommandTask(),
                "show command" to ShowAllCommandTask(),
                "log out" to LogoutTask(),
                "compose message" to ComposeEmailTask(),
                "back" to BackTask()
        )
    }
    abstract fun executeCommand(command: String, activity: MainActivity)
    abstract fun onTTSStart(activity: MainActivity)
    abstract fun onTTSDone(activity: MainActivity)
    abstract fun onTTSError(activity: MainActivity)
    /**
     * Command should be one of the key in the map
     * If match result is greater than 85, it will do the correspondent task
     * @param command the command of user(Recognition result of user voice input).
     * @param activity the activity to perform the task for command.
     */
    override fun executeCommand(command: String, activity: Activity) {
        if (activity is MainActivity) {
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
        throw RuntimeException("MainViewState should only perform in MainActivity.")
    }

    override fun onTTSStart(activity: Activity) {
        if (activity is MainActivity) {
            onTTSStart(activity)
            return
        }
        throw RuntimeException("MainViewState should only perform in MainActivity.")
    }

    override fun onTTSDone(activity: Activity) {
        if (activity is MainActivity) {
            onTTSDone(activity)
            return
        }
        throw RuntimeException("MainViewState should only perform in MainActivity.")
    }

    override fun onTTSError(activity: Activity) {
        if (activity is MainActivity) {
            onTTSError(activity)
            return
        }
        throw RuntimeException("MainViewState should only perform in MainActivity.")
    }
}