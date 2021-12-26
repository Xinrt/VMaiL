package com.teamsixers.vmail.ui.list.listview.idlestate

import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.ListViewTask

/**
 * Compose Email Task wraps up detail task content.
 */
class ComposeEmailTask: ListViewTask() {
    /**
     * Compose Email Task wraps up detail task content.
     */
    override fun doTask(activity: ListActivity) {
        ComposeActivity.actionStart(activity)
    }
}