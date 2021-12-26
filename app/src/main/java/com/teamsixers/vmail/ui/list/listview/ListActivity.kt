package com.teamsixers.vmail.ui.list.listview

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iflytek.cloud.SpeechError
import com.scwang.smartrefresh.layout.SmartRefreshLayout
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener
import com.smailnet.emailkit.EmailKit
import com.smailnet.microkv.MicroKV
import com.teamsixers.vmail.R
import com.teamsixers.vmail.VMailApplication.Companion.config
import com.teamsixers.vmail.databinding.ActivityListBinding
import com.teamsixers.vmail.logic.model.LocalMsg
import com.teamsixers.vmail.ui.BaseActivity
import com.teamsixers.vmail.ui.State
import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.list.MessageAdapter
import com.teamsixers.vmail.ui.list.listview.idlestate.IdleState
import com.teamsixers.vmail.ui.list.listview.readmsgstate.ReadUnreadMessageState
import com.teamsixers.vmail.ui.login.LoginActivity
import com.teamsixers.vmail.utils.Utils
import org.litepal.LitePal


class ListActivity : BaseActivity() {
    override var idleState: State = IdleState()
    lateinit var smartRefreshLayout: SmartRefreshLayout

    val viewModel by lazy {
        ViewModelProvider(
                this, ViewModelProvider.NewInstanceFactory()
        ).get(ListViewModel::class.java)
    }
    private lateinit var binding: ActivityListBinding

    companion object {

        val TAG: String = ListActivity::class.java.simpleName

        fun actionStart(context: Context, folderName: String) {
            val intent = Intent(context, ListActivity::class.java).apply {
                putExtra("folderName", folderName)
            }
            context.startActivity(intent)
        }
    }


    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

        // init view
        binding.activityMainLogoutBtn.setOnClickListener {
            LitePal.deleteAll(LocalMsg::class.java)
            MicroKV.customize("config").clear()
            EmailKit.destroy()
            LoginActivity.actionStart(this)
            finish()
        }
        binding.activityMainComposeBtn.setOnClickListener {
            ComposeActivity.actionStart(this)
        }
        smartRefreshLayout = findViewById(R.id.activity_list_msg_sr)

        smartRefreshLayout.setOnRefreshLoadMoreListener(object : OnRefreshLoadMoreListener {
            override fun onRefresh(@NonNull refreshLayout: RefreshLayout) {
                viewModel.refreshData()
            }

            override fun onLoadMore(@NonNull refreshLayout: RefreshLayout) {
                viewModel.loadData()
            }
        })
        val layoutManager = LinearLayoutManager(this)
        binding.activityListMsgRv.layoutManager = layoutManager
        val adapter = MessageAdapter(viewModel.localMsgList)
        binding.activityListMsgRv.adapter = adapter

        viewModel.folderName = intent.getStringExtra("folderName") ?: ""
        binding.activityListTitleTx.text = viewModel.folderName
        // init data

        // load latest 20 emails without loading content and attachments.
        viewModel.refreshDataResult.observe(this) {
            val localMsgList = it
            if (localMsgList != null) {
                val localUnreadMsgListIterator = viewModel.localUnreadMsgList.iterator()
                while (localUnreadMsgListIterator.hasNext()) {
                    val localUnreadMsg = localUnreadMsgListIterator.next()
                    if (!viewModel.localMsgList.contains(localUnreadMsg)) {
                        localUnreadMsgListIterator.remove()
                    }
                }
                val unreadMsgList = localMsgList.filter { localMsg ->
                    !localMsg.isRead
                }
                viewModel.localUnreadMsgList.clear()
                viewModel.localUnreadMsgList.addAll(unreadMsgList)
                // TODO: could add wait image or audio, and stop here
                binding.activityListMsgRv.visibility = View.VISIBLE
                viewModel.localMsgList.clear()
                viewModel.localMsgList.addAll(localMsgList)
                Log.d(TAG, "localMsgList size: ${localMsgList.size}")


                Log.d("ListActivity", "unread emails: ${viewModel.localUnreadMsgList.size}")
                adapter.notifyDataSetChanged()
                smartRefreshLayout.finishRefresh()
            } else {
                // TODO: No list (Toast or audio)
                voiceAndToast("Sorry, cannot get message now!")
                smartRefreshLayout.finishRefresh()
                Log.w("ListActivity", "Sorry, cannot get message now!")
            }


        }

        // load older 20 emails
        viewModel.loadDataResult.observe(this) { localMsgList ->
            if (localMsgList.isNotEmpty()) {
                // add loaded unread emails into the unread email list
                val olderUnreadMsgList = localMsgList.filter { localMsg ->
                    !localMsg.isRead
                }
                viewModel.localUnreadMsgList.clear()
                viewModel.localUnreadMsgList.addAll(olderUnreadMsgList)
                // TODO: could add wait image or audio, and stop here
                binding.activityListMsgRv.visibility = View.VISIBLE
                // add new loaded message
                viewModel.localMsgList.clear()
                viewModel.localMsgList.addAll(localMsgList)

                Log.d("ListActivity", "unread emails: ${viewModel.localUnreadMsgList.size}")

                adapter.notifyDataSetChanged()
                smartRefreshLayout.finishLoadMore()
            } else {
                // TODO: No list (Toast or audio)
                Utils.toast("Sorry, cannot load more messages now!")
                smartRefreshLayout.finishLoadMore()
            }
            if (state is ReadUnreadMessageState) {
                (state as ReadUnreadMessageState).readLatestTwoUnreadMessages(this)
            }
        }

        viewModel.refreshData()
    }


    override fun onResume() {
        super.onResume()
        // get total email count and unread email count.
        if (viewModel.totalEmailNumber == ListViewModel.MESSAGE_COUNT_UNLOADED
                && viewModel.totalUnreadEmailNumber == ListViewModel.MESSAGE_COUNT_UNLOADED) {
            getTotalAndUnreadEmails()
        } else {
            broadcastEmailCount()
        }
    }

    private fun broadcastEmailCount() {
        textToSpeech("This is ${viewModel.folderName}. " +
                "You totally have ${viewModel.totalEmailNumber} message, " +
                "and ${viewModel.totalUnreadEmailNumber} unread message. " +
                "You can wake up voice assistant and say read messages or unread messages.")
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


    /**
     * TODO: move to logic layer.
     * Get total number of emails and total number of unread emails
     *
     */
    private fun getTotalAndUnreadEmails() {
        EmailKit.useIMAPService(config).getFolder(viewModel.folderName).getMsgCount(object : EmailKit.GetCountCallback {
            override fun onSuccess(totalMessageCount: Int, unreadMessageCount: Int) {
                viewModel.totalEmailNumber = totalMessageCount
                viewModel.totalUnreadEmailNumber = unreadMessageCount
                broadcastEmailCount()
                Log.d("ListActivity", "Total emails: $totalMessageCount, Unread: $unreadMessageCount")
            }

            override fun onFailure(errMsg: String?) {
                Log.d("ListActivity", "cannot get total and unread emails number")
            }
        })
    }

    /**
     * When active listening recognition fails,
     * this method will be the callback method.
     *
     */
    override fun onAsrError(speechError: SpeechError) {
        Log.d("ListActivity", "errorCode ${speechError.errorCode}")
        Log.d("ListActivity", "errorDescription ${speechError.errorDescription}")
    }


    /**
     * When active listening recognition is successfully done, and result is returned.
     * this method will be the callback method.
     * startActiveListening(this) and voice "vmail"  will come to this function.
     */
    override fun onAsrSuccess(recognitionResult: String) {
        state.executeCommand(recognitionResult, this)
    }
}