package com.teamsixers.vmail.ui.list.listview.readmsgstate.openmsgtask

import com.teamsixers.vmail.logic.model.LocalMsg
import com.teamsixers.vmail.ui.detail.DetailActivity
import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.ListViewTask
import com.teamsixers.vmail.ui.list.listview.readmsgstate.ReadMsgState

/**
 * Open message message task wrapper.
 * Subclass should implement specific email list to
 * get message to open.
 */
abstract class OpenMsgTask : ListViewTask() {
    abstract fun getCurrentLocalMsg(currentState: ReadMsgState, activity: ListActivity): LocalMsg
    override fun doTask(activity: ListActivity) {
        val currentState = activity.state
        if (currentState is ReadMsgState) {
            val currentLocalMsg = getCurrentLocalMsg(currentState, activity)
            currentLocalMsg.isRead = true
            currentLocalMsg.save()
            DetailActivity.actionStart(activity, activity.viewModel.folderName, currentLocalMsg.uID)
            return
        }
        throw RuntimeException("Open Msg Task can only perform state of ReadUnreadMessageState and ReadMessageState in ListActivity")
    }
}