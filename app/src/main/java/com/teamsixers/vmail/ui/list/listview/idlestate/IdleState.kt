package com.teamsixers.vmail.ui.list.listview.idlestate

import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.ListViewState

/**
 * See BaseActivity explanation for IdleState.
 */
class IdleState: ListViewState() {
    override fun executeCommand(command: String, activity: ListActivity) {
    }

    override fun onTTSStart(activity: ListActivity) {
    }

    override fun onTTSDone(activity: ListActivity) {
    }

    override fun onTTSError(activity: ListActivity) {
    }
}