package com.teamsixers.vmail.ui.list.listview.readmsgstate.deletemsgtask

import com.teamsixers.vmail.logic.model.LocalMsg
import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.readmsgstate.ReadMsgState
import com.teamsixers.vmail.ui.list.listview.readmsgstate.ReadUnreadMessageState

/**
 * DeleteMsgTask is used for deleting unread messages for differnet states.
 */
class DeleteUnreadMsgTask: DeleteMsgTask() {
    /**
     * get current reading unread message.
     * @param currentState current reading message state.
     */
    override fun getCurrentLocalMsg(currentState: ReadMsgState, activity: ListActivity): LocalMsg {
        if (currentState is ReadUnreadMessageState) {
            return activity.viewModel.localUnreadMsgList[currentState.currentReadingEmailIndexOfLocalMsgList - 1]
        }
        throw RuntimeException("Open UnreadMsgTask can only get local msg from ReadUnreadMessageState.")
    }


}