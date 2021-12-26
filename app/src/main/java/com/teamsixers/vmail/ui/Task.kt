package com.teamsixers.vmail.ui

import android.app.Activity

/**
 * All tasks that need to be completed in the state
 * can be encapsulated in this method.
 * When the task needs to be executed,
 * the doTask method is called
 *
 * @since 2.0
 *
 * @author Mingyan(Cyril) Wang
 */
abstract class Task {

    /**
     * The content of the current task
     * that needs to be executed
     * needs to be implemented in this method
     * @param activity In which activity the current task needs to be executed
     *
     * @since 2.0
     *
     * @author Mingyan(Cyril) Wang
     */
    abstract fun doTask(activity: Activity)

}