package com.teamsixers.vmail.ui.list.listview.readmsgstate.deletemsgtask

import com.teamsixers.vmail.logic.model.LocalMsg
import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.ListViewTask
import com.teamsixers.vmail.ui.list.listview.SwitchState
import com.teamsixers.vmail.ui.list.listview.deletemsgstate.DeleteMsgState
import com.teamsixers.vmail.ui.list.listview.readmsgstate.ReadMsgState


/**
 * DeleteMsgTask is used for deleting messages for differnet states.
 */
abstract class DeleteMsgTask: ListViewTask() {
    abstract fun getCurrentLocalMsg(currentState: ReadMsgState, activity: ListActivity): LocalMsg

    /**
     * Actual delete message task for current state.
     */
    override fun doTask(activity: ListActivity) {
        val currentState = activity.state
        if (currentState is ReadMsgState) {
            val currentLocalMsg = getCurrentLocalMsg(currentState, activity)
            activity.state = object : SwitchState() {
                override fun onYesResponse(activity: ListActivity) {
                    activity.state = DeleteMsgState(currentLocalMsg, currentState)
                    activity.textToSpeech("Deleting email now...")
                }

                override fun onNoResponse(activity: ListActivity) {
                    activity.state = currentState
                    activity.textToSpeech("Stop deleting message. Continue reading.")
                }

                override fun onUnknownResponse(activity: ListActivity) {
                    activity.textToSpeech("You can only say yes for deleting email or no for stop deleting and continue reading email.")
                }

                override fun onTTSStart(activity: ListActivity) {
                }

                override fun onTTSDone(activity: ListActivity) {
                    activity.startActiveListening(activity)
                }

                override fun onTTSError(activity: ListActivity) {
                }

            }
            activity.textToSpeech("Do you want to delete this email with subject ${currentLocalMsg.subject} from ${currentLocalMsg.senderAddress}?")
            return
        }
        throw RuntimeException("Delete Msg Task can only perform state of ReadUnreadMessageState and ReadMessageState in ListActivity")
    }
}