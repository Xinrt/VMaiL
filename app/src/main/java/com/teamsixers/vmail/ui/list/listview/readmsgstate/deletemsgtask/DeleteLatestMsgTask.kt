package com.teamsixers.vmail.ui.list.listview.readmsgstate.deletemsgtask

import com.teamsixers.vmail.logic.model.LocalMsg
import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.readmsgstate.ReadLatestMessageState
import com.teamsixers.vmail.ui.list.listview.readmsgstate.ReadMsgState

/**
 * Delete latest message task wrapper.
 */
class DeleteLatestMsgTask: DeleteMsgTask() {
    /**
     * get current reading message.
     * @param currentState current reading message state.
     */
    override fun getCurrentLocalMsg(currentState: ReadMsgState, activity: ListActivity): LocalMsg {
        if (currentState is ReadLatestMessageState) {
            return activity.viewModel.localMsgList[currentState.currentReadingEmailIndexOfLocalMsgList - 1]
        }
        throw RuntimeException("OpenLastestMsgTask can only get local msg from ReadLatestMessageState.")
    }
}