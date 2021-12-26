package com.teamsixers.vmail.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.teamsixers.vmail.logic.Repository
import com.teamsixers.vmail.logic.model.LocalFile
import com.teamsixers.vmail.logic.model.LocalMsg

/**
 * view model of detail activity
 */
class DetailViewModel : ViewModel(){
    var mainBody: String = ""
    var localFileList= ArrayList<LocalFile>()
    var localMsg: LocalMsg = LocalMsg()
    var folderName: String = ""
    var uid: Long = -1

    private val getLocalMsgLiveData = MutableLiveData<Any?>()

    val getLocalMsgResult = Transformations.switchMap(getLocalMsgLiveData) {
        Repository.getMsg(folderName, uid)
    }

    /**
     * Method should be called after initialize folderName
     */
    fun getLocalMsg() {
        getLocalMsgLiveData.value = getLocalMsgLiveData.value
    }
}