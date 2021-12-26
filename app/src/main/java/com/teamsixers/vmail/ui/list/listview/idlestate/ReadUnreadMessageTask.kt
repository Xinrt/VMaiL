package com.teamsixers.vmail.ui.list.listview.idlestate

import android.util.Log
import com.teamsixers.vmail.ui.compose.IdleState.AddReceiverTask
import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.ListViewTask
import com.teamsixers.vmail.ui.list.listview.readmsgstate.ReadUnreadMessageState

/**
 * Read unread message task wrapper.
 */
class ReadUnreadMessageTask: ListViewTask() {
    companion object {
        val TAG: String = ReadUnreadMessageTask::class.java.simpleName
    }

    /**
     * Actual read unread message task for current state.
     */
    override fun doTask(activity: ListActivity) {
        // change activity state from IdleState to ReadUnreadMessageState
        val readUnreadMessageState = ReadUnreadMessageState()
        activity.state = readUnreadMessageState
        readUnreadMessageState.readLatestTwoUnreadMessages(activity)

        Log.d(AddReceiverTask.TAG, "reading unread message now...")
    }
}