package com.teamsixers.vmail.ui.compose.IdleState

import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.compose.ComposeTask

/**
 * VMaiL will read out all command which users can use
 */
class ShowAllCommandTask: ComposeTask() {
    override fun doTask(activity: ComposeActivity) {
        activity.textToSpeech("You can wake up voice assistant " +
                "and say one of the following command, " +
                "show all commands, send email, back, add carbon copy, " +
                "add blind carbon copy, " +
                "add receiver," +
                "add subject," +
                "add content," +
                "delete word," +
                "delete sentence," +
                "read all," +
                "delete all," +
                "replace word" )
    }
}