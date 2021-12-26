package com.teamsixers.vmail.ui.list.listview

import android.app.Activity
import com.teamsixers.vmail.ui.Task

/**
 * Wrap up Task class for ListActivity.
 */
abstract class ListViewTask: Task() {
    /**
     * Task should be performed in ListActivity.
     */
    abstract fun doTask(activity: ListActivity)

    override fun doTask(activity: Activity) {
        if (activity is ListActivity) {
            doTask(activity)
            return
        }
        throw RuntimeException("list view task should only perform in ListActivity.")
    }
}