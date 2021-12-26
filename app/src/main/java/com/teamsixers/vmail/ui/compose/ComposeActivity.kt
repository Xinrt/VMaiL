package com.teamsixers.vmail.ui.compose

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.iflytek.cloud.SpeechConstant
import com.iflytek.cloud.SpeechError
import com.smailnet.emailkit.Draft
import com.teamsixers.vmail.VMailApplication
import com.teamsixers.vmail.databinding.ActivityComposeBinding
import com.teamsixers.vmail.ui.BaseActivity
import com.teamsixers.vmail.ui.State
import com.teamsixers.vmail.ui.compose.IdleState.IdleState
import com.teamsixers.vmail.utils.StringUtils
import com.teamsixers.vmail.utils.Utils

/**
 * compose activity
 */
class ComposeActivity : BaseActivity() {

    override var idleState: State = IdleState()
    private val viewModel by lazy {
        ViewModelProvider(
                this, ViewModelProvider.NewInstanceFactory()
        ).get(ComposeViewModel::class.java)
    }

    lateinit var binding: ActivityComposeBinding

    /**
     * unified way to start ComposeActivity.
     * Other activity should call ComposeActivity.actionStart(context)
     * to start ComposeActivity.
     */
    companion object {
        val TAG: String = ComposeActivity::class.java.simpleName

        /**
         * Method used for starting Compose Activity
         */
        fun actionStart(context: Context) {
            val intent = Intent(context, ComposeActivity::class.java)
            context.startActivity(intent)
        }
    }

    /**
     * initialize view, data.
     * @param savedInstanceState A mapping from String keys to various Parcelable value
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityComposeBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)
        textToSpeech("Now you can compose your email, you can wake up voice assistant and say show all commands to get the command of compose")
        Log.d("ComposeActivity", "Config: ${VMailApplication.config.isSmtpSTARTTLS}")
        initView()
    }

    /**
     * when text to speech start
     * transmit composeActivity to state
     */
    override fun onTTSStart() {
        state.onTTSStart(this)
    }

    /**
     * when text to speech done
     * transmit composeActivity to state
     */
    override fun onTTSDone() {
        state.onTTSDone(this)
    }

    /**
     * when text to speech occurs an error
     * transmit composeActivity to state
     */
    override fun onTTSError() {
        state.onTTSError(this)
    }

    /**
     * when user come back to this page
     * tell users they can start compose email
     */
    override fun onResume() {
        super.onResume()
        textToSpeech("Now you can compose your email, you can wake up voice assistant and say show all commands to get the command of compose")
    }

    /**
     * When active listening recognition fails,
     * this method will be the callback method.
     */
    override fun onAsrError(speechError: SpeechError) {
        Log.e("ComposeActivity", "${speechError.errorCode}")
        Log.e("ComposeActivity", speechError.errorDescription)
    }


    /**
     * When active listening recognition is successfully done, and result is returned.
     * this method will be the callback method.
     * @since 1.0
     *
     * @author Jiake Hao, Mingyan(Cyril) Wang
     */
    override fun onAsrSuccess(recognitionResult: String) {
        state.executeCommand(recognitionResult, this)
    }


    /**
     * initialize page view
     */
    private fun initView() {
        binding.activityComposeFromAddrTv.text = VMailApplication.config.account

        viewModel.saveEmailResult.observe(this) {
            if (it == true) {
                Utils.toast("Email Saved")
                textToSpeech("Back to previous page")
            } else {
                Utils.toast("Save fails, try again later")
            }
            finish()
        }


        viewModel.sendEmailResult.observe(this) {
            if (it == true) {
                Utils.toast("Sent")
            } else {
                viewModel.saveDraft()
                Utils.toast("Sent fails, try again in draft")
            }
            finish()
        }



        binding.activityComposeCancelBtn.setOnClickListener {
            finish()
        }

    }

    /**
     * reset parameters of speech-to-text
     */
    override fun setAsrParameter() {
        super.setAsrParameter()
        mAsr.setParameter("ptt", "1")
        mAsr.setParameter(SpeechConstant.ASR_PTT, "1")
        mAsr.setParameter("nunum", "0")
    }

    /**
     * check receiver email address, carbonCopy email address, blackCarbonCopy email address are valid or not.
     * check subject and content
     * if it is valid, try to send email.
     */
    fun checkForm() {
        val toAddress = binding.activityComposeToEt.text.toString()
        var recipient = toAddress
        val ccAddress = binding.activityComposeCcEt.text.toString()
        var carbonCopy = ccAddress
        val bccAddress = binding.activityComposeBccEt.text.toString()
        var blackCarbonCopy = bccAddress
        val subjectText = binding.activityComposeSubjectEt.text.toString()
        val content = binding.activityComposeMailContentEt.text.toString()

        if (TextUtils.isEmpty(toAddress) || TextUtils.isEmpty(wakeUpResultString) || TextUtils.isEmpty(content)) {
            voiceAndToast("Please fill up all information!")
        } else {
            val holeContent = binding.activityComposeMailContentEt.text.toString()
            textToSpeech("The content is $holeContent. Waiting for sending")

            while (recipient.contains(" ")) {
                viewModel.recipientsSet.add(StringUtils.getStringBefore(recipient, " "))
                recipient = StringUtils.getStringAfter(recipient, " ")
            }
            viewModel.recipientsSet.add(recipient)

            while (carbonCopy.contains(" ")) {
                viewModel.carbonCopiesSet.add(StringUtils.getStringBefore(carbonCopy, " "))
                carbonCopy = StringUtils.getStringAfter(carbonCopy, " ")
            }
            viewModel.carbonCopiesSet.add(carbonCopy)

            while (blackCarbonCopy.contains(" ")) {
                viewModel.blackCarbonCopiesSet.add(StringUtils.getStringBefore(blackCarbonCopy, " "))
                blackCarbonCopy = StringUtils.getStringAfter(blackCarbonCopy, " ")
            }
            viewModel.blackCarbonCopiesSet.add(blackCarbonCopy)

            val draft: Draft = Draft()
                    .setNickname(VMailApplication.config.nickname)
                    .setTo(viewModel.recipientsSet)
                    .setCc(viewModel.carbonCopiesSet)
                    .setBcc(viewModel.blackCarbonCopiesSet)
                    .setSubject(subjectText)
                    .setText(content)

            viewModel.sendEmail(draft)
        }
    }

    /**
     * Caller should check the validation of email address.
     * @param receiverAddress the email address to be set on UI.
     */
    fun setReceiver(receiverAddress: String) {
        var pastReceiver = ""
        var newReceiver = ""
        try {
            pastReceiver = binding.activityComposeToEt.text.toString()
            newReceiver = if (pastReceiver != "") {
                "$pastReceiver $receiverAddress"
            } else {
                receiverAddress
            }
            binding.activityComposeToEt.setText(newReceiver)
        } catch (e: Exception) {
            Log.e(TAG, "binding should be initialized when setting receiver")
        }
    }

    /**
     * Caller should check the validation of email address.
     * @param ccAddress the email address to be set on UI.
     */
    fun setCarbonCopy(ccAddress: String) {
        var pastCarbonCopy = ""
        var newCarbonCopy = ""
        try {
            pastCarbonCopy = binding.activityComposeCcEt.text.toString()
            newCarbonCopy = if (pastCarbonCopy != "") {
                "$pastCarbonCopy $ccAddress"
            } else {
                ccAddress
            }
            binding.activityComposeCcEt.setText(newCarbonCopy)
        } catch (e: Exception) {
            Log.e(TAG, "binding should be initialized when setting cc")
        }
    }

    /**
     * Caller should check the validation of email address.
     * @param bccAddress the email address to be set on UI.
     */
    fun setBlindCarbonCopy(bccAddress: String) {
        var pastBlackCarbonCopy = ""
        var newBlackCarbonCopy = ""
        try {
            pastBlackCarbonCopy = binding.activityComposeBccEt.text.toString()
            newBlackCarbonCopy = if (pastBlackCarbonCopy != "") {
                "$pastBlackCarbonCopy $bccAddress"
            } else {
                bccAddress
            }
            binding.activityComposeBccEt.setText(newBlackCarbonCopy)
        } catch (e: Exception) {
            Log.e(TAG, "binding should be initialized when setting bcc")
        }
    }

    /**
     * Caller should check the subject.
     * @param subject the subject to be set on UI.
     */
    fun setSubject(subject: String) {
        try {
            binding.activityComposeSubjectEt.setText(subject)
        } catch (e: Exception) {
            Log.e(TAG, "binding should be initialized when setting subject")
        }
    }

    /**
     * Caller should check the content.
     * @param content the content to be set on UI.
     */
    fun setContent(content: String) {
        var pastText = ""
        var newText = ""

        try {
            pastText = binding.activityComposeMailContentEt.text.toString()
            newText = if (pastText == "") {
                "$content."
            } else {
                "$pastText$content."
            }


            binding.activityComposeMailContentEt.setText(newText)
            binding.activityComposeMailContentEt.setSelection(binding.activityComposeMailContentEt.text.length)
        } catch (e: Exception) {
            Log.e(TAG, "binding should be initialized when setting content")
        }
    }
}
