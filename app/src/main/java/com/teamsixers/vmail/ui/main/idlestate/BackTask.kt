package com.teamsixers.vmail.ui.main.idlestate

import com.teamsixers.vmail.ui.main.MainActivity
import com.teamsixers.vmail.ui.main.MainViewTask

/**
 * back to previous page
 */
class BackTask: MainViewTask() {

    /**
     * close this page
     * @param activity The activity to close.
     */
    override fun doTask(activity: MainActivity) {
        activity.finish()
    }
}