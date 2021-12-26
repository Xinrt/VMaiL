package com.teamsixers.vmail.ui.main

/**
 * LogoutTask is used for logout MainActivity.
 */
class LogoutTask: MainViewTask() {
    /**
     * doTask will perform actual logout.
     */
    override fun doTask(activity: MainActivity) {
       activity.logout()
    }
}