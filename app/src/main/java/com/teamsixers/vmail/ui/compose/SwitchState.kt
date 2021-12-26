package com.teamsixers.vmail.ui.compose

/**
 * switch-state of yes/no command
 */
abstract class SwitchState : ComposeState() {
    /**
     * deal with same responds in different situations
     * and give different further instructions
     *
     * @param command input speech text
     * @param activity ComposeActivity
     */
    override fun executeCommand(command: String, activity: ComposeActivity) {
        when {
            command.contains("yes", true) -> {
                onYesResponse(activity)
                return
            }
            command.contains("no", true) -> {
                onNoResponse(activity)
                return
            }
            else -> {
                onUnknownResponse(activity)
                return
            }
        }
    }

    /**
     * further instructions for users after they said "yes"
     * @param activity ComposeActivity
     */
    abstract fun onYesResponse(activity: ComposeActivity)

    /**
     * further instructions for users after they said "no"
     * @param activity ComposeActivity
     */
    abstract fun onNoResponse(activity: ComposeActivity)

    /**
     * further instructions for users if they have no response
     * @param activity ComposeActivity
     */
    abstract fun onUnknownResponse(activity: ComposeActivity)

}