package com.teamsixers.vmail.ui.detail.idlestate

import com.teamsixers.vmail.ui.detail.DetailActivity
import com.teamsixers.vmail.ui.detail.DetailActivityTask

class ShowAllCommandTask: DetailActivityTask() {
    override fun doTask(activity: DetailActivity) {
        activity.textToSpeech("You can wake up voice assistant " +
                "and say one of the following command, " +
                "show all commands, back, read content, " +
                "what is the subject, " +
                "who is the sender," )
    }
}