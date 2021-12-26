package com.teamsixers.vmail.ui.detail

import android.app.Activity
import com.teamsixers.vmail.ui.Task

/**
 * tasks of detail activity
 */
abstract class DetailActivityTask: Task() {
    abstract fun doTask(activity: DetailActivity)

    /**
     * call particular tasks of DetailActivity
     * @param activity Activity should be class or subclass of DetailActivity
     */
    override fun doTask(activity: Activity) {
        if (activity is DetailActivity) {
            doTask(activity)
            return
        }
        throw RuntimeException("detail view task should only perform in DetailActivity.")
    }
}