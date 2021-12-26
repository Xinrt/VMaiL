package com.teamsixers.vmail.ui.voiceSupport

import android.os.Environment
import android.util.Log
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.VoiceWakeuper
import com.iflytek.cloud.WakeuperListener

/**
 * PassiveListeningSupport provides functions for
 * wake up listening.
 * subclass should be activity of android.
 * @since 1.0
 * @author Mingyan(Cyril) Wang
 */
interface PassiveListeningSupport {

    /**
     * VoiceWakeuper instance to perform
     * voice wakeup functions.
     */
    var mIvw: VoiceWakeuper

    /**
     * Wakeuper result string.
     */
    var wakeUpResultString: String

    /**
     * WakeuperListener for current activity.
     * This variable should be overridden by subclass.
     */
    var mWakeUperListener: WakeuperListener

    /**
     * Default mode setting passive listening parameters
     * Subclass can override this method to customize wakeUper
     */
    fun setPassiveListeningParameter() {
        val wakeUpThreshold = 1450
        val isWakeUpKeepAlive = "1"
        val ivwNetMode = "0"
        // Empty parameter
        mIvw.setParameter(SpeechConstant.PARAMS, null)
        // wakeup threshold
        mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:$wakeUpThreshold")
        // wakeup mode
        mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup")
        // setup keep alive listening wakeup hotword
        mIvw.setParameter(SpeechConstant.KEEP_ALIVE, isWakeUpKeepAlive)
        // setup network mode
        mIvw.setParameter(SpeechConstant.IVW_NET_MODE, ivwNetMode)
        // setup wakeup resource path
        mIvw.setParameter(SpeechConstant.IVW_RES_PATH, getResource())
        // setup wakeup audio saved path, and save recently 1 min audio
        mIvw.setParameter(SpeechConstant.IVW_AUDIO_PATH, Environment.getExternalStorageDirectory().path + "/msc/ivw.wav")
        mIvw.setParameter(SpeechConstant.AUDIO_FORMAT, "wav")
    }

    /**
     * Start passive listening for current activity.
     */
    fun startPassiveListening() {
        mIvw = VoiceWakeuper.getWakeuper()
        if (mIvw != null) {
            wakeUpResultString = ""

            setPassiveListeningParameter()
            mIvw.startListening(mWakeUperListener)
        } else {
            Log.w("VoiceWakeUpActivity", "V mail cannot wake up now!")
        }
    }

    /**
     * Wake up needs local resources given by IFLYTEK corresponding to hotword
     */
    fun getResource(): String?

    /**
     * Stop passive listening.
     */
    fun stopPassiveListening() {
        mIvw.stopListening()
        // destroy voice wakeuper
        mIvw = VoiceWakeuper.getWakeuper()
        mIvw.destroy()
    }
}