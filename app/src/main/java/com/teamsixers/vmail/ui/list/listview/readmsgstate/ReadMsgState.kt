package com.teamsixers.vmail.ui.list.listview.readmsgstate

import com.teamsixers.vmail.ui.Task
import com.teamsixers.vmail.ui.list.listview.ListViewState

/**
 * ReadMsgState is an abstract state for reading different messages.
 */
abstract class ReadMsgState: ListViewState() {

    /**
     * Add common command for read latest messages and unread messages.
     * E.g. stop reading command.
     */
    override val cmdTaskMap: HashMap<String, Task>
        get() = super.cmdTaskMap.apply {
            put("Stop reading", StopReadingMsgTask())
        }

    /**
     * indicate the first or second message in the reading group.
     * One group contains two messages.
     * Reading messages group by group.
     * Start from 0 to 1.
     */
    var currentGroupReadingEmailIndex = START_READING_INDEX
    var currentReadingEmailIndexOfLocalMsgList = START_READING_INDEX
    val orderMap by lazy {
        hashMapOf<Int, String>(
                0 to "first",
                1 to "second"
        )
    }
    companion object {
        const val START_READING_INDEX = 0
        const val READING_GROUP_COUNT = 2
    }
}