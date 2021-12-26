package com.teamsixers.vmail.ui.list.listview.deletemsgstate

import com.smailnet.emailkit.EmailKit
import com.smailnet.emailkit.EmailKit.GetOperateCallback
import com.teamsixers.vmail.VMailApplication
import com.teamsixers.vmail.logic.model.LocalMsg
import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.ListViewState


/**
 * @param msgToDelete the message to delete.
 * @param stateToBack the state after deleting will back to.
 */
class DeleteMsgState(private val msgToDelete: LocalMsg, private val stateToBack: ListViewState) : ListViewState() {
    override fun executeCommand(command: String, activity: ListActivity) {
    }

    override fun onTTSStart(activity: ListActivity) {
    }

    override fun onTTSDone(activity: ListActivity) {
        // delete message
        EmailKit.useIMAPService(VMailApplication.config)
                .getFolder(activity.viewModel.folderName)
                .operator()
                .deleteMsg(msgToDelete.uID, object : GetOperateCallback {
                    override fun onSuccess() {
                        onDeleteSuccess(activity)
                    }

                    override fun onFailure(errMsg: String) {
                        onDeleteFailure(activity)
                    }
                })
    }

    /**
     * Perform task on delete message failure.
     */
    fun onDeleteFailure(activity: ListActivity) {
        activity.state = stateToBack
        activity.textToSpeech("Delete fails. Continue reading.")
    }

    /**
     * Perform task on delete message success.
     */
    fun onDeleteSuccess(activity: ListActivity) {
        activity.state = stateToBack
        activity.textToSpeech("Delete successfully.")
        activity.smartRefreshLayout.autoRefresh()
    }

    override fun onTTSError(activity: ListActivity) {
    }
}