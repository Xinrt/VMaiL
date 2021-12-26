package com.teamsixers.vmail.ui.list.listview.readmsgstate.openmsgtask

import com.teamsixers.vmail.logic.model.LocalMsg
import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.readmsgstate.ReadMsgState
import com.teamsixers.vmail.ui.list.listview.readmsgstate.ReadUnreadMessageState

/**
 * Open unread message task wrapper.
 */
class OpenUnreadMsgTask: OpenMsgTask() {
    /**
     * get current unread message.
     */
    override fun getCurrentLocalMsg(currentState: ReadMsgState, activity: ListActivity): LocalMsg {
        if (currentState is ReadUnreadMessageState) {
            return activity.viewModel.localUnreadMsgList[currentState.currentReadingEmailIndexOfLocalMsgList - 1]
        }
        throw RuntimeException("Open UnreadMsgTask can only get local msg from ReadUnreadMessageState.")
    }
}