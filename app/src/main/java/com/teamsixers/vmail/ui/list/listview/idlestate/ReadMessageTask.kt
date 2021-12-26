package com.teamsixers.vmail.ui.list.listview.idlestate

import android.util.Log
import com.teamsixers.vmail.ui.compose.IdleState.AddReceiverTask
import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.ListViewTask
import com.teamsixers.vmail.ui.list.listview.readmsgstate.ReadLatestMessageState

/**
 * Read message task wrapper.
 */
class ReadMessageTask : ListViewTask() {
    companion object {
        val TAG: String = ReadMessageTask::class.java.simpleName
    }

    /**
     * Actual read message task for current state.
     */
    override fun doTask(activity: ListActivity) {
        // change activity state from IdleState to ReadMessageState
        val readMessageState = ReadLatestMessageState()
        activity.state = readMessageState
        readMessageState.readLatestMessage(activity)

        Log.d(AddReceiverTask.TAG, "reading message now...")
    }

}
