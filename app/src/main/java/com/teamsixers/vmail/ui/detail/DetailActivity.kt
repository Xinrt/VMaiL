package com.teamsixers.vmail.ui.detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.iflytek.cloud.SpeechError
import com.teamsixers.vmail.R
import com.teamsixers.vmail.databinding.ActivityDetailBinding
import com.teamsixers.vmail.ui.BaseActivity
import com.teamsixers.vmail.ui.State
import com.teamsixers.vmail.ui.detail.idlestate.IdleState
import io.github.cdimascio.essence.Essence

/**
 * detail activity
 */
class DetailActivity : BaseActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var webView: WebView
    private lateinit var adapter: AttachmentAdapter
    override var idleState: State = IdleState()

    val viewModel by lazy {
        ViewModelProvider(
                this, ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)
    }

    /**
     * unified way to start DetailActivity.
     * Other activity should call DetailActivity.actionStart(context)
     * to start DetailActivity.
     */
    companion object {
        fun actionStart(context: Context, folderName: String, uid: Long) {
            val intent = Intent(context, DetailActivity::class.java).apply {
                putExtra("folderName", folderName)
                putExtra("uid", uid)
            }
            context.startActivity(intent)
        }
    }

    /**
     * initialize view, data.
     * @param savedInstanceState A mapping from String keys to various Parcelable value
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        val rootView = binding.root
        setContentView(rootView)

        viewModel.uid = intent.getLongExtra("uid", -1)
        viewModel.folderName = intent.getStringExtra("folderName").toString()

        initView()

        // call viewModel function to get msg
        initData()

    }

    /**
     * when text to speech start
     * transmit detailActivity to state
     */
    override fun onTTSStart() {
        state.onTTSStart(this)
    }

    /**
     * when text to speech done
     * transmit detailActivity to state
     */
    override fun onTTSDone() {
        state.onTTSDone(this)
    }

    /**
     * when text to speech occur an error
     * transmit detailActivity to state
     */
    override fun onTTSError() {
        state.onTTSError(this)
    }


    /**
     * When active listening recognition fails,
     * this method will be the callback method.
     */
    override fun onAsrError(speechError: SpeechError) {
    }

    /**
     * When active listening recognition is successfully done, and result is returned.
     * this method will be the callback method.
     */
    override fun onAsrSuccess(recognitionResult: String) {
        state.executeCommand(recognitionResult, this)
    }

    /**
     * initialize message data
     */
    private fun initData() {
        viewModel.getLocalMsg()
    }

    /**
     * initialize page view
     */
    private fun initView() {
        progressBar = findViewById(R.id.activity_detail_progressBar)
        initContentView()
        initAttachmentListView()
        // callback listener to populate view
        initMsgCallBack()
    }

    /**
     * initialize attachment list view
     */
    private fun initAttachmentListView(){
        val layoutManager = LinearLayoutManager(this)
        binding.activityDetailAttachmentRw.layoutManager = layoutManager
        adapter = AttachmentAdapter(viewModel.localFileList)
        binding.activityDetailAttachmentRw.adapter = adapter
    }

    /**
     * get message information and assign to text
     */
    @SuppressLint("SetTextI18n")
    private fun initMsgCallBack() {
        viewModel.getLocalMsgResult.observe(this) { localMsg ->
            viewModel.localMsg = localMsg
            if (localMsg.subject != "") {
                binding.activityDetailSubjectTv.text = localMsg.subject
            } else {
                binding.activityDetailSubjectTv.text = "No subject"
            }
            if (localMsg.senderNickname != "") {
                binding.activityDetailsSender.text = localMsg.senderNickname
            } else {
                binding.activityDetailsSender.text = localMsg.senderAddress
            }
            binding.activityDetailsDate.text = localMsg.date
            binding.activityDetailsReceiversTx.text = localMsg.recipientNickname

            val text = localMsg.text
            val type = localMsg.type
            val mainBody = Essence.extract(viewModel.localMsg.text)
            viewModel.mainBody = if (mainBody.text.isEmpty()) {
                    "The email main body is empty or contains images, which cannot be translated into voice now."
                } else {
                    mainBody.text
                }
            textToSpeech(viewModel.mainBody)
            Log.d("DetailActivity", mainBody.toString())
            webView.loadDataWithBaseURL(
                    null,
                    adaptScreen(text, type),
                    "text/html",
                    "utf-8",
                    null
            )
            viewModel.localFileList.clear()
            val nonInlineAttachment = localMsg.localFileList.filter {
                !it.isInline
            }
            viewModel.localFileList.addAll(nonInlineAttachment)
            adapter.notifyDataSetChanged()
        }
    }

    /**
     * initialize message content view
     */
    private fun initContentView() {
        webView = findViewById(R.id.activity_detail_content_wv)
        val webSettings = webView.settings
        webSettings.loadsImagesAutomatically = true
        webSettings.javaScriptEnabled = false

        webSettings.loadsImagesAutomatically = true

        webSettings.useWideViewPort = true
        webSettings.loadWithOverviewMode = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false
        webView.isHorizontalScrollBarEnabled = false
        webView.isVerticalScrollBarEnabled = false
        webView.setInitialScale(25)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress == 100 || viewModel.localMsg.isCached) {
                    progressBar.visibility = View.GONE
                }
            }
        }
    }


    /**
     * Adapt screen
     *
     * @param text main text of an email
     * @param type type of main text
     *
     * @return return adapted version of HTML text
     */
    private fun adaptScreen(text: String, type: String): String {
        return if (type == "text/html") {
            """<html>
                <head>
                    <meta name="viewport" content="width=device-width,initial-scale=1">
                </head>
                <body> $text </body>
                </html>"""
        } else {
            """<html>
                <head>
                    <meta name="viewport" content="width=device-width,initial-scale=1">
                </head>
                <body> <font size="3">$text</font> </body>
                </html>"""
        }
    }

}