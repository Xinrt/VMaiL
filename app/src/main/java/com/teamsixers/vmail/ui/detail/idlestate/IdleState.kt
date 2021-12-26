package com.teamsixers.vmail.ui.detail.idlestate

import com.teamsixers.vmail.ui.detail.DetailActivity
import com.teamsixers.vmail.ui.detail.DetailActivityState

/**
 * idle state
 */
class IdleState: DetailActivityState() {
    /**
     * empty function
     * @param command input speech text
     * @param activity DetailActivity
     */
    override fun executeCommand(command: String, activity: DetailActivity) {

    }

    /**
     * empty function
     * @param activity DetailActivity
     */
    override fun onTTSStart(activity: DetailActivity) {
    }

    /**
     * empty function
     * @param activity DetailActivity
     */
    override fun onTTSDone(activity: DetailActivity) {
    }

    /**
     * empty function
     * @param activity DetailActivity
     */
    override fun onTTSError(activity: DetailActivity) {
    }
}