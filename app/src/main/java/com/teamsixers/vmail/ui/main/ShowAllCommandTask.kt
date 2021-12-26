package com.teamsixers.vmail.ui.main

/**
 * Show all command Task in MainActivity.
 */
class ShowAllCommandTask: MainViewTask() {
    /**
     * doTask method will perform show all command tasks detail action
     * @param activity MainActivity to perform show all command tasks.
     */
    override fun doTask(activity: MainActivity) {
        activity.textToSpeech("You can wake up voice assistant and say one of the following command, " +
                "open folder, show all commands, log out, " +
                "compose message and back for backing to previous page.")
    }
}