package com.teamsixers.vmail.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iflytek.cloud.SpeechError
import com.smailnet.emailkit.EmailKit
import com.smailnet.microkv.MicroKV
import com.teamsixers.vmail.VMailApplication
import com.teamsixers.vmail.databinding.ActivityMainBinding
import com.teamsixers.vmail.logic.model.LocalMsg
import com.teamsixers.vmail.ui.BaseActivity
import com.teamsixers.vmail.ui.State
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.login.LoginActivity
import com.teamsixers.vmail.ui.main.idlestate.IdleState
import org.litepal.LitePal.deleteAll

/**
 * folder page
 */
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding


    override var idleState: State = IdleState()

    val viewModel by lazy {
        ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)
    }



    /**
     * start MainActivity.
     */
    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }

    /**
     * initialize view, data.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)


        initView()
    }

    override fun onTTSStart() {
        state.onTTSStart(this)
    }

    override fun onTTSDone() {
        state.onTTSDone(this)
    }

    override fun onTTSError() {
        state.onTTSError(this)

    }

    override fun onResume() {
        super.onResume()
        readFolder()
    }

    /**
     * When active listening recognition fails,
     * this method will be the callback method.
     *
     * * @since 1.0
     *
     * @author Xinran Tang
     */
    override fun onAsrError(speechError: SpeechError) {
        Log.d("MainActivity", "errorCode ${speechError.errorCode}")
        Log.d("MainActivity", "errorDescription ${speechError.errorDescription}")
    }

    /**
     * When active listening recognition is successfully done, and result is returned.
     * this method will be the callback method.
     *
     * * @since 1.0
     *
     * @author Xinran Tang
     */
    override fun onAsrSuccess(recognitionResult: String) {
        state.executeCommand(recognitionResult, this)
    }



    /**
     * Logout account and back to login page
     */
    fun logout(){
        deleteAll(LocalMsg::class.java)
        MicroKV.customize("config").clear()
        EmailKit.destroy()
        LoginActivity.actionStart(this)
        finish()
    }

    /**
     * VMaiL reads out all folder names
     *
     * * @since 1.0
     *
     * @author Xinran Tang
     */
    private fun readFolder() {
        textToSpeech("This is Folder page. " +
                "You can wake up voice assistant and " +
                "say read folder list or show all commands"
        )
    }

    /**
     * Initialize folder page view
     */
    private fun initView() {
        binding.activityMainLogoutBtn.setOnClickListener {
            deleteAll(LocalMsg::class.java)
            MicroKV.customize("config").clear()
            EmailKit.destroy()
            LoginActivity.actionStart(this)
            finish()
        }

        binding.activityMainComposeBtn.setOnClickListener {
            ComposeActivity.actionStart(this)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.activityMainFoldersItemRcView.layoutManager = layoutManager
        val adapter = FolderAdapter(viewModel.folderList)
        binding.activityMainFoldersItemRcView.adapter = adapter

        viewModel.folderLiveData.observe(this) {
            val folders = it?.toMutableList()
            if (folders != null) {
                binding.activityMainFoldersItemRcView.visibility = View.VISIBLE
                viewModel.folderList.clear()
                viewModel.folderList.addAll(folders)
                adapter.notifyDataSetChanged()
            } else {
                voiceAndToast("Cannot get any folders in your email account")
            }
        }

        viewModel.getDefaultFolderList(VMailApplication.config)
    }

}
