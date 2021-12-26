package com.teamsixers.vmail.ui.list.listview

/**
 * switch-state of yes/no command
 */
abstract class SwitchState : ListViewState() {

    /**
     * deal with same responds in different situations
     * and give different further instructions
     *
     * @param command input speech text
     * @param activity ListActivity
     */
    override fun executeCommand(command: String, activity: ListActivity) {
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
     * @param activity ListActivity
     */
    abstract fun onYesResponse(activity: ListActivity)

    /**
     * further instructions for users after they said "no"
     * @param activity ListActivity
     */
    abstract fun onNoResponse(activity: ListActivity)

    /**
     * further instructions for users if they have no response
     * @param activity ListActivity
     */
    abstract fun onUnknownResponse(activity: ListActivity)

}
