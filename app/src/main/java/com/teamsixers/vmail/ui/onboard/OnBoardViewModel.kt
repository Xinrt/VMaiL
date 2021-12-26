package com.teamsixers.vmail.ui.onboard

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.teamsixers.vmail.logic.Repository

/**
 * Perform data access task for OnBoardActivity.
 */
class OnBoardViewModel : ViewModel(){

    /**
     * LiveData for checkLogin functions.
     */
    private val checkLoginLiveData = MutableLiveData<Any?>()

    /**
     * LiveData for getUserConfig functions.
     */
    private val getUserConfigLiveData = MutableLiveData<Any?>()

    /**
     * Actually, check login will perform in this MutableLiveData.
     * checkLoginLiveData will check whether the set method is called.
     * If it is called, it will call repository method.
     */
    val checkLoginResult = Transformations.switchMap(checkLoginLiveData) {
        Repository.isLogin()
    }

    /**
     * Actually, getUserConfig will perform in this MutableLiveData.
     * getUserConfig will get user configuration.
     * If it is called, it will call repository method.
     */
    val getUserConfigResult = Transformations.switchMap(getUserConfigLiveData) {
        Repository.getUserConfig()
    }

    /**
     * check login provides function for activity to check login or not.
     */
    fun checkLogin() {
        checkLoginLiveData.value = checkLoginLiveData.value
    }

    /**
     * getUserConfig provides function for activity to
     * get user configuration.
     */
    fun getUserConfig() {
        getUserConfigLiveData.value = getUserConfigLiveData.value
    }
}