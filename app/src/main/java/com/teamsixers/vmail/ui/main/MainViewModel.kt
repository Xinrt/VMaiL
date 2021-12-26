package com.teamsixers.vmail.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smailnet.emailkit.EmailKit
import com.teamsixers.vmail.logic.Repository

/**
 * MainViewModel provides Repository layer access for
 * MainActivity and store data of MainActivity.
 */
class MainViewModel : ViewModel() {

    /**
     * ConfigLiveData for current ViewModel.
     */
    private val configLiveData = MutableLiveData<EmailKit.Config>()

    /**
     * Folder list of current email account.
     */
    val folderList = ArrayList<String>()

    val folderLiveData = Transformations.switchMap(configLiveData) {
        Repository.getDefaultFolderList(it)
    }

    /**
     * getDefaultFolderList provides function for
     * activity to get default folder list.
     */
    fun getDefaultFolderList(config: EmailKit.Config) {
        configLiveData.value = config
    }


}