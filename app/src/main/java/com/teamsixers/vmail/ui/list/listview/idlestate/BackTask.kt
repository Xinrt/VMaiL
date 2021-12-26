package com.teamsixers.vmail.ui.list.listview.idlestate

import android.util.Log
import com.teamsixers.vmail.ui.list.listview.ListActivity
import com.teamsixers.vmail.ui.list.listview.ListViewTask

/**
 * back to previous page
 */
class BackTask: ListViewTask() {

    /**
     * close this page
     * @param activity The activity to close.
     */
    override fun doTask(activity: ListActivity) {
        activity.finish()
        Log.d("com.teamsixers.vmail.ui.compose.IdleState.SendEmailTask", "Sending email now...")
    }
}