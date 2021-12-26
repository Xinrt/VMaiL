package com.teamsixers.vmail.ui.main

import android.app.Activity
import com.teamsixers.vmail.ui.Task

/**
 * Wrap up Task class for MainActivity.
 */
abstract class MainViewTask: Task() {
    /**
     * Task should be performed in MainActivity.
     */
    abstract fun doTask(activity: MainActivity)

    override fun doTask(activity: Activity) {
        if (activity is MainActivity) {
            doTask(activity)
            return
        }
        throw RuntimeException("main view task should only perform in MainActivity.")
    }
}