package com.teamsixers.vmail.ui.login


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.iflytek.cloud.SpeechError
import com.smailnet.emailkit.EmailKit
import com.teamsixers.vmail.VMailApplication
import com.teamsixers.vmail.databinding.ActivityLoginBinding
import com.teamsixers.vmail.ui.BaseActivity
import com.teamsixers.vmail.ui.State
import com.teamsixers.vmail.ui.list.listview.idlestate.IdleState
import com.teamsixers.vmail.ui.main.MainActivity


/**
 * LoginActivity is login page for user to login.
 */
class LoginActivity: BaseActivity() {
    /**
     * view binding for LoginActivity.
     */
    private lateinit var binding: ActivityLoginBinding

    /**
     * ViewModel for loginActivity.
     * ViewModel provides access to Repository layer and
     * store LoginActivity data in memory.
     */
    private lateinit var viewModel: LoginViewModel

    override var idleState: State = IdleState()

    /**
     * unified way to start LoginActivity.
     * Other activity should call LoginActivity.actionStart(context)
     * to start LoginActivity.
     *
     * @since 1.0
     *
     * @author Mingyan Wang
     *
     */
    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }

    /**
     * initialize view, data.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)


        viewModel = ViewModelProvider(
                this, ViewModelProvider.NewInstanceFactory()
        ).get(LoginViewModel::class.java)

        binding.activityLoginLoginBtn.setOnClickListener {
            checkForm()
        }

        viewModel.isAuthSuccess.observe(this) {
            if (it) {
                textToSpeech("authentication success!")
                Log.d("LoginActivity", "Config starttls: ${VMailApplication.config.isSmtpSTARTTLS}")
                viewModel.saveUserConfig(VMailApplication.config)
            } else {
                textToSpeech("authentication fails!")
            }
        }

        viewModel.isSaveConfigSuccess.observe(this) {
            if (it) {
                Log.i("LoginActivity", "Save config success!")
                MainActivity.actionStart(this)
                finish()
            } else {
                Log.i("LoginActivity", "Save config fail!")
            }
        }
    }

    /**
     * perform nothing when TextToSpeech starts.
     */
    override fun onTTSStart() {
    }

    /**
     * perform nothing when TextToSpeech done.
     */
    override fun onTTSDone() {
    }

    /**
     * perform nothing when TextToSpeech error.
     */
    override fun onTTSError() {
    }

    /**
     * When active listening recognition fails,
     * this method will be the callback method.
     * @since 1.0
     *
     * @author Mingyan Wang
     */
    override fun onAsrError(speechError: SpeechError) {
        Log.d("error", speechError.toString())
    }


    /**
     * When active listening recognition is successfully done, and result is returned.
     * this method will be the callback method.
     * @since 1.0
     *
     * @author Mingyan Wang
     */
    override fun onAsrSuccess(recognitionResult: String) {
        state.executeCommand(recognitionResult,this)
    }

    override fun onResume() {
        super.onResume()
        textToSpeech("Please fill up your account" +
                "password, nickname by typing. " +
                "We don't provide voice interaction for this activity because of security reason.")
    }

    /**
     * check account, password nickname, mailType are valid or not.
     * if it is valid, try to login user account.
     *
     * @since 1.0
     *
     * @author Mingyan Wang
     *
     */
    private fun checkForm() {
        val account = binding.activityLoginAccountEt.text.toString()
        val password = binding.activityLoginPasswordEt.text.toString()
        val nickname = binding.activityLoginNicknameEt.text.toString()
        var mailType = ""
        if (binding.activityLoginOffice365Cb.isChecked) {
            mailType = EmailKit.MailType.OFFICE365
        }


        if (TextUtils.isEmpty(mailType) || TextUtils.isEmpty(account) ||
            TextUtils.isEmpty(password) || TextUtils.isEmpty(nickname)) {
            voiceAndToast("Please fill up all contents!")
        } else {
            val config = EmailKit.Config()
                .setMailType(mailType)
                .setAccount(account)
                .setPassword(password)
                .setNickname(nickname)
            Log.d("LoginActivity", "Config setup: ${config.isSmtpSTARTTLS}")
            VMailApplication.config = config
            viewModel.authEmailAccount(config)
        }
    }

}
