package com.teamsixers.vmail.ui.detail.idlestate

import com.teamsixers.vmail.ui.detail.DetailActivity
import com.teamsixers.vmail.ui.detail.DetailActivityTask

/**
 * read subject
 */
class ReadSubjectTask: DetailActivityTask() {
    /**
     * call textToSpeech method
     * @param activity DetailActivity
     */
    override fun doTask(activity: DetailActivity) {
        activity.textToSpeech(activity.viewModel.localMsg.subject)
    }
}