package com.teamsixers.vmail.ui.list.listview.idlestate

import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.ListViewTask

/**
 * Show all commands task wrapper.
 */
class ShowAllCommandTask: ListViewTask() {

    /**
     * Actual show all commands task for current state.
     */
    override fun doTask(activity: ListActivity) {
        activity.textToSpeech("Current page has the following command. " +
                "Read unread message, read message and at the same time, you can say delete message or open this message,compose message and back for backing previous page.")
    }
}