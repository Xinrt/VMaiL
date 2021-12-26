package com.teamsixers.vmail.ui.main.idlestate

import com.teamsixers.vmail.ui.main.MainActivity
import com.teamsixers.vmail.ui.main.MainViewState

class IdleState: MainViewState() {
    override fun executeCommand(command: String, activity: MainActivity) {
        activity.textToSpeech("Sorry, I don't understand $command")
    }

    override fun onTTSStart(activity: MainActivity) {

    }

    override fun onTTSDone(activity: MainActivity) {
    }

    override fun onTTSError(activity: MainActivity) {
    }
}