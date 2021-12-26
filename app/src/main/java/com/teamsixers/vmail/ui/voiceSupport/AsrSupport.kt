package com.teamsixers.vmail.ui.voiceSupport

import android.content.Context
import android.util.Log
import com.iflytek.cloud.*
import java.util.*

/**
 * AsrSupport provides functions for
 * auto speech recognition.
 *
 * @since 1.0
 *
 * @author Mingyan Wang
 */
interface AsrSupport {
    /**
     * listener should be overridden by subclass.
     */
    val mRecogListener: RecognizerListener

    /**
     * auto speech recognition result of each sentence
     * will stored in this hashmap.
     */
    val mAsrResults: HashMap<Int, String>

    /**
     * auto speech recognition instance.
     */
    var mAsr: SpeechRecognizer

    /**
     * initialize listener for auto speech recognition.
     */
    val mInitListener: InitListener

    /**
     * Start active listening, which means
     * starting voice recognition and process voice result
     * @param context the context to start active listening, e.g. activity
     */
    fun startActiveListening(context: Context) {
        startActiveListening(context, mRecogListener)
    }

    /**
     * Start active listening, which means
     * starting voice recognition and process voice result with customized listener.
     * @param context the context to start active listening, e.g. activity
     */
    fun startActiveListening(context: Context, recogListener: RecognizerListener) {
        mAsr.startListening(recogListener)
        Log.d("AsrSupport", "Start ASR...")
    }

    /**
     * Set up speech to text parameter
     *
     * Function can be overridden by subclass for specific purpose
     * For example, for numbers, can choose prefer result to be english word or Arabic numerals
     *
     * @author Xinran Tang, Mingyan Wang
     *
     * @since 2.0
     */
    fun setAsrParameter() {
        mAsr.setParameter(SpeechConstant.DOMAIN, "iat")
        mAsr.setParameter(SpeechConstant.LANGUAGE, "en_us")
        mAsr.setParameter(SpeechConstant.RESULT_TYPE, "json");
        mAsr.setParameter("nunum", "1")
        mAsr.setParameter("ptt", "1")
        mAsr.setParameter(SpeechConstant.VAD_EOS, "1800")
        mAsr.setParameter(SpeechConstant.VAD_BOS, "4000")
        mAsr.setParameter(SpeechConstant.ASR_PTT, "1")
    }



    /**
     * Stop active listening for current activity active listening
     * @since 1.0
     *
     * @author Mingyan Wang
     *
     */
    fun stopActiveListening() {
        mAsr.stopListening()
        mAsr.cancel()
    }


    /**
     * Subclass should implement this method to indicate what action will
     * perform when active listening encounters error.
     */
    fun onAsrError(speechError: SpeechError)

    /**
     * When active listening recognition is successfully done, and result is returned.
     * this method will be the callback method.
     * Subclass should implement this method to indicate what action will
     * perform when active listening successes.
     * @since 1.0
     *
     * @author Mingyan Wang
     */
    fun onAsrSuccess(recognitionResult: String)
}