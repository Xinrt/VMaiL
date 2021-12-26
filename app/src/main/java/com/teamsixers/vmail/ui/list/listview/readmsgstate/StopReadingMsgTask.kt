package com.teamsixers.vmail.ui.list.listview.readmsgstate

import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.ListViewTask

/**
 * StopReadingMsgState is the state for stop reading messages.
 */
class StopReadingMsgTask: ListViewTask() {
    override fun doTask(activity: ListActivity) {
        activity.state = activity.idleState
        activity.textToSpeech("stop reading messages now.")
        return
    }
}