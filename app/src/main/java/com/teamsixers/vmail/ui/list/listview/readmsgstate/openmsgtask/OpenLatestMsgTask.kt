package com.teamsixers.vmail.ui.list.listview.readmsgstate.openmsgtask

import com.teamsixers.vmail.logic.model.LocalMsg
import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.readmsgstate.ReadLatestMessageState
import com.teamsixers.vmail.ui.list.listview.readmsgstate.ReadMsgState

/**
 * Open latest message task wrapper.
 */
class OpenLatestMsgTask: OpenMsgTask() {
    /**
     * get current local message of current reading latest message.
     */
    override fun getCurrentLocalMsg(currentState: ReadMsgState, activity: ListActivity): LocalMsg {
        if (currentState is ReadLatestMessageState) {
            return activity.viewModel.localMsgList[currentState.currentReadingEmailIndexOfLocalMsgList - 1]
        }
        throw RuntimeException("OpenLatestMsgTask can only get local msg from ReadLatestMessageState.")
    }
}