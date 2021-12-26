package com.teamsixers.vmail.ui.voiceSupport

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import com.teamsixers.vmail.VMailApplication.Companion.context
import com.teamsixers.vmail.utils.Utils
import java.util.*

/**
 * TextToSpeechSupport provides text to speech support for activity.
 * The function is out of box for activity usage.
 *
 * @since 1.0
 *
 * @author Mingyan Wang(Cyril)
 */
interface TextToSpeechSupport {
    /**
     * textToSpeechEngine provides all textToSpeech basic functions.
     */
    var textToSpeechEngine: TextToSpeech

    /**
     * Receive input text and speech the text
     * This method cannot register a listener when finishing text to speech.
     * @param inputText Text to speech
     * @since 1.0
     * @author Mingyan(Cyril) Wang
     * @sample textToSpeech("Good Morning")
     */
    fun textToSpeech(inputText: String) {
        textToSpeechEngine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeechEngine.language = Locale.US
                val text = inputText.trim()
                if (text.isNotEmpty()) {
                    textToSpeechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1")
                }
            }
            return@TextToSpeech
        }
        val text = inputText.trim()
        if (text.isNotEmpty()) {
            textToSpeechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1")
        } else {
            Toast.makeText(context, "Text cannot be empty", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * Receive input text, speech the text and perform register a listener specified by listener.
     * <p> This method can register a listener
     * when it starts, error, and finishes text to speech. </p>
     *
     * @param inputText Text to speech.
     * @param utteranceProgressListener Listener for onStart, onError, onFinish TTS.
     * @since 2.0
     * @author Mingyan(Cyril) Wang
     * @sample textToSpeech("Good Morning", utteranceProgressListener)
     */
    fun textToSpeech(inputText: String, utteranceProgressListener: UtteranceProgressListener) {
        textToSpeechEngine = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeechEngine.setOnUtteranceProgressListener(utteranceProgressListener)
                textToSpeechEngine.language = Locale.US
                val text = inputText.trim()
                if (text.isNotEmpty()) {
                    textToSpeechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1")
                }
            } else {
                Log.e("com.teamsixers.vmail.ui.voiceSupport.TextToSpeechSupport", "text to speech engine cannot be initialized.")
            }
            return@TextToSpeech
        }
        textToSpeechEngine.setOnUtteranceProgressListener(utteranceProgressListener)
        val text = inputText.trim()
        if (text.isNotEmpty()) {
            textToSpeechEngine.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts1")
        } else {
            Toast.makeText(context, "Text cannot be empty", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * @param inputText the string to speak and make toast.
     * @context context the caller context, e.g. activity.
     *
     * @sample  `voiceAndToast`("Hello", this)
     *
     * @since 1.0
     *
     * @author Mingyan(Cyril) Wang
     */
    fun voiceAndToast(inputText: String) {
        textToSpeech(inputText)
        Utils.toast(inputText)
    }

    /**
     * Interrupts the current utterance
     * and discards other utterances in the queue.
     * Caller should check whether textToSpeech is initialized or not.
     * @since 2.0
     * @author Mingyan(Cyril) Wang
     */
    fun stopTextToSpeech() {
        textToSpeechEngine.stop()
        textToSpeechEngine.shutdown()
    }

}