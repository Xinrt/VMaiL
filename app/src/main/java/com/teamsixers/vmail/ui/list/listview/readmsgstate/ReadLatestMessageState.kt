package com.teamsixers.vmail.ui.list.listview.readmsgstate

import com.teamsixers.vmail.ui.Task
import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.SwitchState
import com.teamsixers.vmail.ui.list.listview.idlestate.IdleState
import com.teamsixers.vmail.ui.list.listview.readmsgstate.deletemsgtask.DeleteLatestMsgTask
import com.teamsixers.vmail.ui.list.listview.readmsgstate.openmsgtask.OpenLatestMsgTask

/**
 * ReadLatestMessageState is the state for reading latest messages.
 * This state provides tasks for open latest messages and delete latest messages.
 */
class ReadLatestMessageState: ReadMsgState() {
    override val cmdTaskMap: HashMap<String, Task>
        get() = super.cmdTaskMap.apply {
            put("Open this message", OpenLatestMsgTask())
            put("Delete this message", DeleteLatestMsgTask())
        }

    /**
     * Read one latest message
     * In current state(ReadMessageState), if globally read two messages(one group),
     * ask user whether continue reading or stop reading.
     * If haven't finished yet reading one group(two messages), continue reading next message.
     */
    fun readLatestMessage(activity: ListActivity) {

        // Check whether have totally read two messages(one group).
        // Haven't read two unread messages, continue reading.
        if (currentGroupReadingEmailIndex < READING_GROUP_COUNT) {
            // read one message at a time.

            // No more messages globally(server), stop reading.
            if (currentReadingEmailIndexOfLocalMsgList > activity.viewModel.totalEmailNumber - 1) {
                activity.state = activity.idleState
                activity.textToSpeech("No more messages now.")
                return
            }

            // Still have messages.
            // If locally haven't cached, load from server
            if (currentReadingEmailIndexOfLocalMsgList > activity.viewModel.localMsgList.size - 1) {
                activity.state = LoadMoreMsgState(this)
                activity.textToSpeech("Please wait for loading for more unread message from server.")
                return
            }
            // If locally have cached unread messages, just read it.
            val localMsg = activity.viewModel.localMsgList[currentReadingEmailIndexOfLocalMsgList]

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
        val currentStateLatest: ReadLatestMessageState = activity.state as ReadLatestMessageState
        activity.state = object : SwitchState() {
            /**
             * onYesResponse(activity) user wants to continue reading message.
             */
            override fun onYesResponse(activity: ListActivity) {
                // set current activity state back to ReadMessageState
                activity.state = currentStateLatest
                // reset reading email index in group from 0
                currentStateLatest.currentGroupReadingEmailIndex = START_READING_INDEX
                // continue reading
                currentStateLatest.readLatestMessage(activity)
            }

            override fun onNoResponse(activity: ListActivity) {
                activity.state = IdleState()
                activity.textToSpeech("Stop reading message. Activity now is idle.")
            }

            override fun onUnknownResponse(activity: ListActivity) {
                activity.textToSpeech("You can only say yes or no to continue or stop reading message. ")
            }

            override fun onTTSStart(activity: ListActivity) {
            }

            override fun onTTSDone(activity: ListActivity) {
                activity.startActiveListening(activity)
            }

            override fun onTTSError(activity: ListActivity) {
            }
        }
        activity.textToSpeech("Do you want to listen to next two messages?")
        return
    }

    override fun executeCommand(command: String, activity: ListActivity) {
    }

    override fun onTTSStart(activity: ListActivity) {
    }

    override fun onTTSDone(activity: ListActivity) {
        readLatestMessage(activity)
    }

    override fun onTTSError(activity: ListActivity) {
    }

}