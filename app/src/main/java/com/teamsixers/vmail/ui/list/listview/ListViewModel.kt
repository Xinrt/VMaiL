package com.teamsixers.vmail.ui.list.listview

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.teamsixers.vmail.logic.Repository
import com.teamsixers.vmail.logic.model.LocalMsg

/**
 * ListViewModel provides Repository layer access for
 * ListActivity and store data of ListActivity.
 */
class ListViewModel : ViewModel(){

    /**
     * folderName of current list activity folder.
     */
    var folderName = ""

    /**
     * local messages list for current list activity.
     */
    var localMsgList = ArrayList<LocalMsg>()

    /**
     * local unread messages list for current list activity.
     */
    var localUnreadMsgList = ArrayList<LocalMsg>()

    /**
     * Total email number of current folder.
     */
    var totalEmailNumber: Int = MESSAGE_COUNT_UNLOADED

    /**
     * Total unread email number of current folder.
     */
    var totalUnreadEmailNumber: Int = MESSAGE_COUNT_UNLOADED

    private val refreshLiveData = MutableLiveData<Any?>()
    private val loadLiveData = MutableLiveData<Any?>()

    companion object {
        /**
         * UID for loading messages when messages are not loaded at all.
         */
        const val MESSAGE_COUNT_UNLOADED = -1
    }

    val refreshDataResult = Transformations.switchMap(refreshLiveData) {
        Repository.getNewMsg(folderName)
    }

    val loadDataResult = Transformations.switchMap(loadLiveData) {
        Repository.loadMoreOldMsg(folderName)
    }

    /**
     * Refresh current folder.
     */
    fun refreshData() {
        refreshLiveData.value = refreshLiveData.value
    }

    /**
     * load more older messages for current folder.
     */
    fun loadData() {
        loadLiveData.value = loadLiveData.value
    }



}