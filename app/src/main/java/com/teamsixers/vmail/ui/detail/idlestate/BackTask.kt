package com.teamsixers.vmail.ui.detail.idlestate

import com.teamsixers.vmail.ui.detail.DetailActivity
import com.teamsixers.vmail.ui.detail.DetailActivityTask

/**
 * back to previous page
 */
class BackTask: DetailActivityTask() {
    /**
     * close DetailActivity
     * @param activity goal activity
     */
    override fun doTask(activity: DetailActivity) {
        activity.finish()
    }
}