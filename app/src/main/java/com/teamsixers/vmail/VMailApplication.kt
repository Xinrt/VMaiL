package com.teamsixers.vmail

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechUtility
import com.smailnet.emailkit.EmailKit
import com.smailnet.microkv.MicroKV
import org.litepal.LitePal

/**
 * The Application class for VMail App.
 * Global constant or configuration should be initialized in this class.
 */
class VMailApplication : Application() {

    companion object {
        /**
         * Store global context
         * provide way for accessing Context globally in project
         */
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context

        /**
         * Current Email App configuration
         * The configuration contains user account,
         * password, and email type etc.
         */
        lateinit var config: EmailKit.Config
    }

    /**
     * Initialize framework functionality
     * for this VMail application.
     */
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
        LitePal.initialize(this)
        EmailKit.initialize(this)
        MicroKV.initialize(this)
        SpeechUtility.createUtility(context, "${SpeechConstant.APPID}=" + getString(R.string.app_id))
    }

}