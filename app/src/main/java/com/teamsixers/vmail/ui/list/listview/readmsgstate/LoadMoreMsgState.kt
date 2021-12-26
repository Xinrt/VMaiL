package com.teamsixers.vmail.ui.list.listview.readmsgstate

import com.blankj.utilcode.util.ThreadUtils.runOnUiThread
import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.ListViewState

/**
 * LoadMoreMsgState is the state for loading more messages from server.
 * Only perform action onTTSDone.
 */
class LoadMoreMsgState(private val readMsgState: ListViewState) : ListViewState() {
    override fun executeCommand(command: String, activity: ListActivity) {
    }

    override fun onTTSStart(activity: ListActivity) {
    }

    override fun onTTSDone(activity: ListActivity) {
        activity.state = readMsgState
        runOnUiThread {
            // Stuff that updates the UI
            activity.smartRefreshLayout.autoLoadMore()
        }
    }

    override fun onTTSError(activity: ListActivity) {
    }
}