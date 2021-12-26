package com.teamsixers.vmail.ui.main

import com.teamsixers.vmail.ui.list.listview.ListActivity
import me.xdrop.fuzzywuzzy.FuzzySearch
import me.xdrop.fuzzywuzzy.model.ExtractedResult

/**
 * Open Folder State in MainActivity.
 * @since 2.0
 * @author Mingyan(Cyril) Wang
 */
class OpenFolderState : MainViewState() {
    /**
     * Command should be folderName. Otherwise notify user to re-input.
     */
    override fun executeCommand(command: String, activity: MainActivity) {
        val folderList = activity.viewModel.folderList

        val findResult = FuzzySearch.extractOne(command, folderList)
        if (findResult.score > 85) {
            // find corresponding folder, open it.
            openFolder(activity, findResult)
            return
        }

        activity.textToSpeech("Cannot find folder $command, please try again." +
                "Your current email has following folders, $folderList.")
    }

    fun openFolder(activity: MainActivity, findResult: ExtractedResult) {
        ListActivity.actionStart(activity, findResult.string)
    }

    override fun onTTSStart(activity: MainActivity) {
    }

    override fun onTTSDone(activity: MainActivity) {
        activity.startActiveListening(activity)
    }

    override fun onTTSError(activity: MainActivity) {

    }
}