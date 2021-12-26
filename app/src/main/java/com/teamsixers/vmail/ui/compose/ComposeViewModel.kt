package com.teamsixers.vmail.ui.compose

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smailnet.emailkit.Draft
import com.teamsixers.vmail.logic.Repository

/**
 * compose view model
 */
class ComposeViewModel : ViewModel() {

    var recipientsSet: MutableSet<String> = HashSet()
    var carbonCopiesSet: MutableSet<String> = HashSet()
    var blackCarbonCopiesSet: MutableSet<String> = HashSet()


    private val saveEmailLiveData = MutableLiveData<Draft>()
    private val getUserConfigLiveData = MutableLiveData<Any?>()
    private val sendEmailLiveData = MutableLiveData<Draft>()

    lateinit var draft: Draft

    val getUserConfigResult = Transformations.switchMap(getUserConfigLiveData) {
        Repository.getUserConfig()
    }

    val sendEmailResult = Transformations.switchMap(sendEmailLiveData) {
        Repository.sendEmail(it)
    }

    val saveEmailResult = Transformations.switchMap(saveEmailLiveData) {
        Repository.saveEmail(it)
    }

    fun getUserConfig() {
        getUserConfigLiveData.value = getUserConfigLiveData.value
    }

    /**
     * caller can only pass non-null Draft
     */
    fun sendEmail(draft: Draft) {
        this.draft = draft
        sendEmailLiveData.value = draft
    }

    /**
     * current email can be saved as draft
     */
    fun saveDraft() {
        if (::draft.isInitialized) {
            saveEmailLiveData.value = this.draft
        } else {
            throw RuntimeException("Draft cannot be empty")
        }
    }
}