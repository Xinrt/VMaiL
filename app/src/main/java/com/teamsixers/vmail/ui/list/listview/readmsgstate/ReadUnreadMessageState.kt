package com.teamsixers.vmail.ui.list.listview.readmsgstate

import com.teamsixers.vmail.ui.Task
import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.SwitchState
import com.teamsixers.vmail.ui.list.listview.idlestate.IdleState
import com.teamsixers.vmail.ui.list.listview.readmsgstate.deletemsgtask.DeleteUnreadMsgTask
import com.teamsixers.vmail.ui.list.listview.readmsgstate.openmsgtask.OpenUnreadMsgTask

/**
 * ReadUnreadMessageState is the state for reading unread messages in INBOX.
 */
class ReadUnreadMessageState : ReadMsgState() {

    override val cmdTaskMap: HashMap<String, Task>
        get() = super.cmdTaskMap.apply {
            put("Open this message", OpenUnreadMsgTask())
            put("Stop reading", StopReadingMsgTask())
            put("Delete this message", DeleteUnreadMsgTask())
        }


    /**
     * Reading one unread message if haven't read two messages yet.
     * If have read two messages, ask user whether continue reading next two messages.
     *
     * When next unread message to be read is not cached locally, load it from server.
     * When unread messages have all been read,
     * notify user that there is no more unread messages and stop reading unread messages.
     * @param activity ListActivity
     *
     * @since 2.0
     *
     * @author Mingyan(Cyril) Wang
     */
    fun readLatestTwoUnreadMessages(activity: ListActivity) {


        // Check whether have totally read two unread messages.
        // Haven't read two unread messages, continue reading.
        if (currentGroupReadingEmailIndex < READING_GROUP_COUNT) {
            // set index of current message
            // read one message at a time.

            // No more unread message, stop reading.
            if (currentReadingEmailIndexOfLocalMsgList > activity.viewModel.totalUnreadEmailNumber - 1) {
                activity.state = activity.idleState
                activity.textToSpeech("No more unread message now.")
                return
            }
            // Still have unread messages.
            // If locally haven't cached, load from server
            if (currentReadingEmailIndexOfLocalMsgList > activity.viewModel.localUnreadMsgList.size - 1) {
                activity.state = LoadMoreMsgState(this)
                activity.textToSpeech("Please wait for loading for more unread message from server.")
                return
            }
            // If locally have cached unread messages, just read it.
            val localMsg = activity.viewModel.localUnreadMsgList[currentReadingEmailIndexOfLocalMsgList]

            val from = if (localMsg.senderNickname != "") {
                localMsg.senderNickname
            } else {
                localMsg.senderAddress
            }
            val subject = if (localMsg.subject != "") {
                localMsg.subject
            } else {
                "is empty"
            }
            val order = orderMap[currentGroupReadingEmailIndex]
            // update current reading email index in localMsgList and index of the first or second message to read.
            currentReadingEmailIndexOfLocalMsgList++
            currentGroupReadingEmailIndex++
            activity.textToSpeech("The $order message from $from with subject $subject. ")
            return
        }

        // If have read two messages, ask user whether continue reading or not.
        val currentState: ReadUnreadMessageState = activity.state as ReadUnreadMessageState
        activity.state = object : SwitchState() {
            override fun onYesResponse(activity: ListActivity) {
                activity.state = currentState
                currentState.currentGroupReadingEmailIndex = START_READING_INDEX
                currentState.readLatestTwoUnreadMessages(activity)
            }

            override fun onNoResponse(activity: ListActivity) {
                activity.state = IdleState()
                activity.textToSpeech("Stop reading unread message. Activity now is idle.")
            }

            override fun onUnknownResponse(activity: ListActivity) {
                activity.textToSpeech("Would you like to listen to next couple messages?")
            }

            override fun onTTSStart(activity: ListActivity) {
            }

            override fun onTTSDone(activity: ListActivity) {
                activity.startActiveListening(activity)
            }

            override fun onTTSError(activity: ListActivity) {
            }
        }
        activity.textToSpeech("Do you want to listen to next two unread messages?")
        return
    }

    override fun executeCommand(command: String, activity: ListActivity) {
        activity.textToSpeech("You can only say operation about message now.")
        return
    }

    override fun onTTSStart(activity: ListActivity) {
    }

    override fun onTTSDone(activity: ListActivity) {
        readLatestTwoUnreadMessages(activity)
    }

    override fun onTTSError(activity: ListActivity) {
    }

}