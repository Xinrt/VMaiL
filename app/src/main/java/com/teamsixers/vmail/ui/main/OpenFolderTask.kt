package com.teamsixers.vmail.ui.main


/**
 * Open Folder Task in MainActivity.
 * @since 2.0
 * @author Mingyan(Cyril) Wang
 */
class OpenFolderTask: MainViewTask() {

    /**
     * doTask method will perform open folder tasks detail action
     * @param activity MainActivity to perform task.
     */
    override fun doTask(activity: MainActivity) {
        activity.state = OpenFolderState()
        activity.textToSpeech("Which folder you want to open?")
    }
}