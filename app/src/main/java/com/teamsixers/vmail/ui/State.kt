package com.teamsixers.vmail.ui

import android.app.Activity

/**
 * As the state of an activity,
 * the subclass can customize the functions that need to be performed in its state,
 * such as the end of voice recognition and how to process the voice content
 * @since 2.0
 * @author Mingyan(Cyril) Wang
 */
abstract class State {
    /**
     * How to process the command after the speech recognition is completed
     * should be implemented in this method.
     * @param command the recognition result of user input command.
     * @param activity the activity to execute command
     * @since 2.0
     * @author Mingyan(Cyril) Wang
     */
    abstract fun executeCommand(command: String, activity: Activity)

    /**
     * The tasks to be completed after the speech to text
     * begins need to be implemented in this method
     * @param activity the activity to do the task.
     * @since 2.0
     * @author Mingyan(Cyril) Wang
     */
    abstract fun onTTSStart(activity: Activity)


    /**
     * The tasks to be completed after the speech to text
     * is completed need to be implemented in this method
     * @param activity the activity to do the task.
     * @since 2.0
     * @author Mingyan(Cyril) Wang
     */
    abstract fun onTTSDone(activity: Activity)

    /**
     * The tasks to be completed after the speech to text
     * is on the error state need to be implemented in this method
     * @param activity the activity to do the task.
     * @since 2.0
     * @author Mingyan(Cyril) Wang
     */
    abstract fun onTTSError(activity: Activity)
}