package com.teamsixers.vmail.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.smailnet.emailkit.EmailKit
import com.teamsixers.vmail.logic.Repository


/**
 * LoginViewModel provides Repository layer access for
 * LoginActivity and store data of LoginActivity.
 */
class LoginViewModel : ViewModel(){

    /**
     * LiveData for email account to authentication.
     */
    private val configLiveDataToAuth = MutableLiveData<EmailKit.Config>()

    /**
     * LiveData for email account to save.
     */
    private val configLiveDataToSave = MutableLiveData<EmailKit.Config>()

    /**
     * Perform actual authentication of email account.
     * See Android official documentation for liveData and switchMap.
     */
    val isAuthSuccess = Transformations.switchMap(configLiveDataToAuth) { config ->
        Repository.authMailAccount(config)
    }


    /**
     * Perform actual save configuration of email account.
     * See Android official documentation for liveData and switchMap.
     */
    val isSaveConfigSuccess = Transformations.switchMap(configLiveDataToSave) { config ->
        Repository.saveUserConfig(config)
    }

    /**
     * authEmailAccount provides save user config function for activity layer.
     * @config configuration of user information, such as password, email account, email type.
     */
    fun authEmailAccount(config: EmailKit.Config) {
        configLiveDataToAuth.value = config
    }

    /**
     * saveUserConfig provides save user config function for activity layer.
     * @config configuration of user information, such as password, email account, email type.
     */
    fun saveUserConfig(config: EmailKit.Config) {
        configLiveDataToSave.value = config
    }
}
