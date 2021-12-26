package com.teamsixers.vmail.ui

import android.Manifest
import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.iflytek.cloud.*
import com.iflytek.cloud.util.ResourceUtil
import com.permissionx.guolindev.PermissionX
import com.teamsixers.vmail.R
import com.teamsixers.vmail.ui.voiceSupport.AsrSupport
import com.teamsixers.vmail.ui.voiceSupport.PassiveListeningSupport
import com.teamsixers.vmail.ui.voiceSupport.TextToSpeechSupport
import com.teamsixers.vmail.utils.JsonParser
import org.json.JSONException
import org.json.JSONObject
import java.util.*

/**
 * All activities that needs TextToSpeech, Auto Speech Recognition, and
 * voice wakeup support in the VMail should inherit this class.
 *
 * Subclass should override idleState, and assign the IdleState instance
 */
abstract class BaseActivity : AppCompatActivity(),
        InitListener,
        AsrSupport,
    PassiveListeningSupport,
    TextToSpeechSupport {


    companion object
    {
        /**
         * TAG is used for logger
         */
        val TAG: String = this::class.java.simpleName
    }

    /**
     * MediaPlayer for starting ASR notification.
     */
    lateinit var mStartAsrMediaPlayer: MediaPlayer

    /**
     * MediaPlayer for end ASR notification.
     */
    lateinit var mEndAsrMediaPlayer: MediaPlayer

    /**
     * Voice wakuper instacne
     */
    override lateinit var mIvw: VoiceWakeuper

    /**
     * Auto Speech Recognition instance.
     */
    override lateinit var mAsr: SpeechRecognizer

    /**
     * The idle state for every activity.
     * When no task is performed in the activity,
     * the activity should change to this state.
     *
     * idleState of each activity can be different.
     */
    open lateinit var idleState: State

    /**
     * Current state of current activity.
     */
    open lateinit var state: State

    /**
     * Temporary storage for recognition result to convert
     * result to string.
     */
    override val mAsrResults: HashMap<Int, String> = LinkedHashMap()

    /**
     * WakeuperListener is used for registering funtions when
     * wakuper state is changed.
     */
    override var mWakeUperListener: WakeuperListener = object : WakeuperListener {
        /**
         * When wakuper is wake up, start auto speech recognition.
         */
        override fun onResult(result: WakeuperResult) {
            Log.d(TAG, "Wakuper onResult Function start")
            try {
                val text = result.resultString
                val jsonObject = JSONObject(text)
                val buffer = StringBuffer().apply {
                    append("[RAW] $text")
                    append("\n")
                    append("[Operation type]" + jsonObject.optString("sst"))
                    append("\n")
                    append("wakeup id" + jsonObject.optString("id"))
                    append("\n")
                    append("[Score]" + jsonObject.optString("score"))
                    append("\n")
                    append("[Front End]" + jsonObject.optString("bos"))
                    append("\n")
                    append("[End point]" + jsonObject.optString("eos"))
                }
                wakeUpResultString = buffer.toString()
                // stop TTS
                if (::textToSpeechEngine.isInitialized) {
                    textToSpeechEngine.stop()
                }
                if (::mAsr.isInitialized) {
                    mAsr.stopListening()
                }
                startActiveListening(this@BaseActivity)

            } catch (e: JSONException) {
                wakeUpResultString = "Voice wakeup fails parsing"
                e.printStackTrace()
            }
            Log.d(TAG, "ResultString: $wakeUpResultString")
        }

        /**
         * When wakuper is on the error state, print error message.
         */
        override fun onError(error: SpeechError) {
            Log.d(TAG, "ErrorCode: ${error.errorCode}")
            Log.d(TAG, "ErrorDesc: ${error.errorDescription}")
        }

        /**
         * Nothing performed when begin of speech.
         */
        override fun onBeginOfSpeech() {}

        /**
         * Response to different events.
         */
        override fun onEvent(eventType: Int, isLast: Int, arg2: Int, obj: Bundle) {
            when (eventType) {
                SpeechEvent.EVENT_RECORD_DATA -> {
                    val audio = obj.getByteArray(SpeechEvent.KEY_EVENT_RECORD_DATA)
                    Log.i("LoginActivity", "ivw audio length: " + audio!!.size)
                }
            }
        }

        /**
         * Perform nothing on volume change.
         */
        override fun onVolumeChanged(volume: Int) {}
    }

    /**
     * The wake up string.
     */
    override var wakeUpResultString: String = ""

    /**
     * Text to speech Engine.
     */
    override lateinit var textToSpeechEngine: TextToSpeech

    /**
     * BaseActivity onCreate state will initialize
     * ASR, wakuper, and requests permissions.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize ASR recognition singleton
        mAsr = SpeechRecognizer.createRecognizer(this, mInitListener)
        setAsrParameter()
        // Initialize Wakeuper singleton
        mIvw = VoiceWakeuper.createWakeuper(this, null)
        mStartAsrMediaPlayer = MediaPlayer.create(this, R.raw.notifysound)
        mEndAsrMediaPlayer = MediaPlayer.create(this, R.raw.endasrsound)
        mStartAsrMediaPlayer.setOnCompletionListener {
            Log.d("BaseActivity", "on complete playing notfiy sound...")
            super.startActiveListening(this)
        }
        PermissionX.init(this)
            .permissions(Manifest.permission.RECORD_AUDIO)
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                } else {
                    Toast.makeText(this, "These permissions are denied: $deniedList", Toast.LENGTH_LONG).show()
                }
            }
    }

    /**
     * Change to auto speech recognition listening state.
     * Start listening to user input voice for recognition.
     */
    override fun startActiveListening(context: Context) {
        if (::textToSpeechEngine.isInitialized) {
            textToSpeechEngine.stop()
            textToSpeechEngine.shutdown()
        }
        mStartAsrMediaPlayer.start()
    }

    /**
     * Wrap up Text to speech function in TextToSpeechSupport.
     * BaseActivity should stop auto speech recognition listening state
     * to avoid recording the voice-to-text sound during voice recognition
     *
     * Method should be called from activity.
     *
     * @sample textToSpeech("Hello from VMail")
     *
     * @author Mingyan(Cyril) Wang
     *
     * @since 2.0
     */
    override fun textToSpeech(inputText: String) {
        // Before starting TTS, make sure stop ASR.
        mAsr.stopListening()
        mAsr.cancel()
        super.textToSpeech(inputText, utteranceProgressListener)
    }

    /**
     * Start TTS, end TTS, and TTS error will execute the corresponding function.
     * The tasks to be completed in these three corresponding states should be implemented
     * onTTSStart, onTTSDone, and onTTSError functions to complete.
     * Specifically, what to do should be implemented by subclass acitvities.
     *
     * @author Mingyan(Cyril) Wang
     *
     * @since 2.0
     */
    private val utteranceProgressListener = object: UtteranceProgressListener() {
        override fun onStart(utteranceId: String?) {
            onTTSStart()
        }

        override fun onDone(utteranceId: String?) {
            onTTSDone()
        }

        override fun onError(utteranceId: String?) {
            onTTSError()
        }

    }

    /**
     * When text to speech starts, the method will be called.
     * @since 2.0
     *
     * @author Mingyan(Cyril) Wang
     */
    abstract fun onTTSStart()

    /**
     * When text to speech finishes, the method will be called.
     * @since 2.0
     *
     * @author Mingyan(Cyril) Wang
     */
    abstract fun onTTSDone()

    /**
     * When text to speech encounters an error, the method will be called.
     * @since 2.0
     *
     * @author Mingyan(Cyril) Wang
     */
    abstract fun onTTSError()


    /**
     * on initialize ASR listener.
     *
     * @since 2.0
     *
     * @author Mingyan(Cyril) Wang
     */
    override fun onInit(p0: Int) {
    }

    /**
     * Wake up needs local resources given by IFLYTEK corresponding to hotword
     *
     * @since 2.0
     *
     * @author Mingyan(Cyril) Wang
     */
    override fun getResource(): String? {
        val resPath = ResourceUtil.generateResourcePath(this,
                ResourceUtil.RESOURCE_TYPE.assets,
                "ivw/" + getString(R.string.app_id) + ".jet")
        Log.d("LoginActivity", "resPath: $resPath")
        return resPath
    }

    /**
     * activity will change to idleState when onResume state
     *
     * @since 2.0
     *
     * @author Mingyan(Cyril) Wang
     */
    override fun onResume() {
        super.onResume()
        if (idleState != null) {
            state = idleState
        } else {
            throw RuntimeException("idleState variable should be assigned value.")
        }
        startPassiveListening()
    }

    /**
     * onPause activity should stop ASR, TTS, Wakeuper.
     *
     * @since 2.0
     *
     * @author Mingyan(Cyril) Wang
     */
    override fun onPause() {
        super.onPause()
        Log.d("BaseActivity", "onPause is called")
        // stop wakeuper
        if (mIvw != null) {
            mIvw.stopListening()
            mIvw.cancel()
        }
        // stop textToSpeech
        if (::textToSpeechEngine.isInitialized) {
            textToSpeechEngine.stop()
            textToSpeechEngine.shutdown()
        }
        // stop ASR (voice recognition)
        if (mAsr != null) {
            // stop listening firstly.
            mAsr.stopListening()
            // cancel current ASR conversation,
            // discarding results that have not returned yet.
            mAsr.cancel()
        }
    }

    /**
     * onDestroy activity should free ASR resources.
     *
     * @since 2.0
     *
     * @author Mingyan(Cyril) Wang
     */
    override fun onDestroy() {
        super.onDestroy()
        // release connection to voice recognition when exits current activity.
        if (null != mAsr) {
            // 退出时释放连接
            mAsr.cancel()
            mAsr.destroy()
        }
    }


    /**
     * Initialize ASR initialized listener.
     *
     * @since 2.0
     *
     * @author Mingyan(Cyril) Wang
     */
    override val mInitListener = InitListener { code ->
        Log.d(TAG, "SpeechRecognizer init() code = $code")
        if (code == ErrorCode.SUCCESS) {
            Log.d(TAG, "SpeechRecognizer init() success!")
        } else {
            Log.e(TAG, "Initialization failure, error code：$code, click https://www.xfyun.cn/document/error-code to look for solution.")
        }
    }

    /**
     * Initialize ASR listener.
     */
    override val mRecogListener: RecognizerListener = object : RecognizerListener {
        override fun onVolumeChanged(p0: Int, p1: ByteArray?) {
        }

        override fun onBeginOfSpeech() {
        }

        /**
         * Play notification sound on end of speech recording.
         *
         * stopListening() function will not call this function.
         *
         * @since 2.0
         *
         * @author Mingyan(Cyril) Wang
         *
         */
        override fun onEndOfSpeech() {
            mEndAsrMediaPlayer.start()

        }

        /**
         * When get recognition and parse json to a result string,
         * this method will be called.
         * This method will call onAsrSuccess()
         *
         * @since 2.0
         *
         * @author Mingyan(Cyril) Wang
         */
        override fun onResult(asrResult: RecognizerResult, isLast: Boolean) {
            if (!isLast) {
                JsonParser.getResultString(mAsrResults, asrResult)
                for (key in mAsrResults.keys) {
                    val recognitionResult: String = if (mAsrResults[key] == null) {
                        ""
                    } else {
                        mAsrResults[key].toString()
                    }
                    Log.d(TAG, "recognitionResult: $recognitionResult")

                    onAsrSuccess(recognitionResult)
                }
            }
        }

        override fun onError(speechError: SpeechError) {
            onAsrError(speechError)
        }

        override fun onEvent(p0: Int, p1: Int, p2: Int, p3: Bundle?) {
        }
    }


}