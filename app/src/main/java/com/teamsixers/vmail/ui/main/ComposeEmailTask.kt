package com.teamsixers.vmail.ui.main

import com.teamsixers.vmail.ui.compose.ComposeActivity

/**
 * Compose Email Task wraps up detail task content.
 */
class ComposeEmailTask: MainViewTask() {

    /**
     * doTask provides function for do actual compose activity task.
     */
    override fun doTask(activity: MainActivity) {
        ComposeActivity.actionStart(activity)
    }
}