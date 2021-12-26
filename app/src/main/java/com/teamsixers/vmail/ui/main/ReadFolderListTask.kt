package com.teamsixers.vmail.ui.main

/**
 * Read Folder List Task in MainActivity.
 */
class ReadFolderListTask: MainViewTask() {

    /**
     * doTask method will perform read folder list tasks detail action
     * @param activity MainActivity to perform task.
     */
    override fun doTask(activity: MainActivity) {
        val folderList = activity.viewModel.folderList.toString()
        activity.textToSpeech("Your message account has the following folders, $folderList")
    }
}