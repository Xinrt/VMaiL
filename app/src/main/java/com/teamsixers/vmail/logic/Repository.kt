package com.teamsixers.vmail.logic

import android.util.Log
import androidx.lifecycle.liveData
import com.smailnet.emailkit.Draft
import com.smailnet.emailkit.EmailKit
import com.smailnet.emailkit.Message
import com.teamsixers.vmail.VMailApplication.Companion.context
import com.teamsixers.vmail.logic.dao.LocalMsgDao
import com.teamsixers.vmail.logic.dao.LocalMsgDao.getLocalMsgList
import com.teamsixers.vmail.logic.dao.LocalMsgDao.getLocalUidArray
import com.teamsixers.vmail.logic.dao.LocalMsgDao.saveLocalMsgList
import com.teamsixers.vmail.logic.dao.UserDao
import com.teamsixers.vmail.logic.dao.UserDao.getFolderList
import com.teamsixers.vmail.logic.model.LocalFile
import com.teamsixers.vmail.logic.model.LocalMsg
import com.teamsixers.vmail.logic.network.IMAPService
import com.teamsixers.vmail.logic.network.VMailNetwork
import kotlinx.coroutines.Dispatchers
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import org.litepal.LitePal.saveAll
import java.util.*


/**
 * Repository layer provides functions for ViewModel layer.
 *
 * Repository layer will implement functions based on DAO layer and network layer.
 */
object Repository {
    val TAG: String = Repository::class.java.simpleName

    /**
     * Authentication of an email account
     *
     * @param config an email account configuration
     * including email type, account, password, SSL etc @see EmailKit.Config
     *
     * @sample Repository.authMailAccount(config)
     *
     * @author Mingyan Wang(Cyril)
     *
     * @since 1.0
     *
     * @return return LiveData true if authentication successes.
     * If authentication fails or exception encounters, return false.
     */
    fun authMailAccount(config: EmailKit.Config) = liveData<Boolean> {
        val result = try {
            val isAuthSuccess = VMailNetwork.authMailAccount(config)
            if (isAuthSuccess) {
                Result.success(true)
            } else {
                Result.failure(RuntimeException("Email Account is not valid"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
        emit(result.getOrDefault(false))
    }


    /**
     * Get new local messages will send request to mail server for synchronization.
     * New messages will be fetched to local database and outdated message will be deleted in local database.
     *
     * @param folderName The folder to get new local messages
     *
     * @sample getNewMsg("INBOX")
     *
     * @author Mingyan Wang(Cyril)
     *
     * @since 1.0
     *
     * @return if get new messages success, return new local message.
     * If fail, return origin local message list, which could be empty.
     */
    fun getNewMsg(folderName: String) = liveData<List<LocalMsg>>(Dispatchers.IO) {
        val localMsgList = getLocalMsgList(folderName)
        val isLocalMsgListEmpty = localMsgList.isEmpty()
        val folder = IMAPService.getFolderByName(folderName)
        val lastUID = if (isLocalMsgListEmpty) EmailKit.NO_CACHED_UID else localMsgList[localMsgList.size - 1].uID
        Log.d("Repository", "folder name is $folderName")
        Log.d("Repository", "lastUID is $lastUID")
        val result = try {
            if (isLocalMsgListEmpty) {
                val responseMessage = VMailNetwork.loadMsg(lastUID, folder)
                saveLocalMsgList(folderName, responseMessage)
                val newLocalMsgList = getLocalMsgList(folderName)
                Result.success(newLocalMsgList)
            } else {
                val localUidArray = getLocalUidArray(folderName)
                val (newMsgList, deleteUIDArray) = VMailNetwork.syncMsg(folder, localUidArray)

                // delete message in local
                for (uid in deleteUIDArray) {
                    for (localMsg in localMsgList) {
                        if (uid == localMsg.uID) {
                            localMsg.delete()
                            localMsgList.remove(localMsg)
                            break
                        }
                    }
                }
                saveLocalMsgList(folderName, newMsgList)
                val newLocalMsgList = getLocalMsgList(folderName)
                Result.success(newLocalMsgList)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
        emit(result.getOrDefault(localMsgList))
    }

    /**
     * Load more message that are received older than the oldest message in local storage.
     *
     * @param folderName The folder to get more older local messages
     *
     * @sample Repository.loadMoreOldMsg("INBOX")
     *
     * @author Mingyan Wang(Cyril)
     *
     * @since 1.0
     *
     * @return if get more messages success, return the more local messages.
     * If fail, return origin local message list, which could be empty.
     */
    fun loadMoreOldMsg(folderName: String) = liveData<List<LocalMsg>> {
        val localMsgList = getLocalMsgList(folderName)
        val isLocalMsgListEmpty = localMsgList.isEmpty()
        val folder = IMAPService.getFolderByName(folderName)
        val lastUID = if (isLocalMsgListEmpty) EmailKit.NO_CACHED_UID else localMsgList[localMsgList.size - 1].uID
        val result = try {
            val responseMessage = VMailNetwork.loadMsg(lastUID, folder)
            saveLocalMsgList(folderName, responseMessage)
            Result.success(getLocalMsgList(folderName))
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
        emit(result.getOrDefault(localMsgList))
    }


    /**
     * Save user login information only.
     * This method can only be called from viewModel layer.
     *
     * @param config an email account configuration
     * including email type, account, password, SSL etc @see EmailKit.Config
     *
     * @sample Repository.saveUserConfig(config)
     *
     * @author Mingyan Wang(Cyril)
     *
     * @since 1.0
     *
     * @return return LiveData true if save user configuration successes.
     * If saving process fails or exception encounters, return false.
     */
    fun saveUserConfig(config: EmailKit.Config) = liveData<Boolean>(Dispatchers.IO) {
        val result = try {
            val isSaveUserConfig = UserDao.saveUserConfig(config)
            if (isSaveUserConfig) {
                Result.success(true)
            } else {
                Result.failure(RuntimeException("Save config fail"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
        emit(result.getOrDefault(false))
    }

    /**
     * Caller should check whether user config exists or not.
     * getUserConfig will assume User config exists.
     *
     * @sample `if` isUserConfigSaved() { val config = getUserConfig() }
     *
     * @return If user config exists, return saved user config.
     * If no user config exists, return a empty user config.
     */
    fun getUserConfig() = liveData(Dispatchers.IO) {
        val result = try {
            val userConfig = UserDao.getUserConfig()
            Result.success(userConfig)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
        emit(result.getOrDefault(EmailKit.Config()))
    }

    /**
     * getDefaultFolderList will check whether folder list is cached or not.
     * If cached, load from local storage. If not cached, load from server and save them locally.
     * This method can only called from ViewModel layer, not in activity.
     *
     * @return If local storage has cached folder list, load from local storage.
     * Otherwise, load from server.
     */
    fun getDefaultFolderList(config: EmailKit.Config) = liveData(Dispatchers.IO) {
        val result: Result<List<String>> = if (UserDao.isFolderListSaved()) {
            Result.success(getFolderList())
        } else {
            try {
                val defaultFolderList = VMailNetwork.getDefaultFolderList(config)
                if (defaultFolderList.isNotEmpty()) {
                    Result.success(defaultFolderList)
                } else {
                    Result.failure(RuntimeException("Email Folder List is not reachable now"))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Result.failure(e)
            }
        }
        emit(result.getOrDefault(emptyList()))

    }

    fun isLogin() = liveData(Dispatchers.IO) {
        val result = try {
            val isUserConfigSaved = UserDao.isUserConfigSaved()
            Result.success(isUserConfigSaved)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
        emit(result.getOrDefault(false))
    }

    /**
     * Get a local message from local storage
     * Method can only be called from viewModel layer.
     *
     * @since 1.0
     *
     * @author Mingyan Wang(Cyril)
     *
     * @sample getLocalMsg("INBOX", -1)
     *
     * @param folderName The folder that message belongs to
     * @param uid The uid of message within the folder
     *
     * @return If find a message in given condition, return the message.
     * Otherwise, return an empty message.
     */
    fun getLocalMsg(folderName: String, uid: Long) = liveData(Dispatchers.IO) {
        val result = try {
            val localMsg = LocalMsgDao.getLocalMsg(folderName, uid)
            Result.success(localMsg)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
        emit(result.getOrDefault(LocalMsg()))
    }

    /**
     * Get a local message from local storage if main body and attachment list are cached.
     * If not cached, load from mail server. And save attachment list in local storage.
     * Change src inside message body to downloaded resources path in local storage,
     * for example, <img src="cid:image001.png@452345"/> will be changed to
     * <img src="/attachments/uniqueTimestamp+filename">. This new path within src
     * is the inline resource downloaded to local storage.
     *
     * Method can only be called from viewModel layer.
     *
     * @since 1.0
     *
     * @author Mingyan Wang(Cyril)
     *
     * @sample getMsg("INBOX", 15213)
     *
     * @param folderName The folder that message belongs to
     * @param uid The uid of message within the folder
     *
     * @return If target message is cached, return cached message.
     * If not cached, return new processed message from mail server.
     */
    fun getMsg(folderName: String, uid: Long) = liveData(Dispatchers.IO) {
        val result = try {
            val localMsg = LocalMsgDao.getLocalMsg(folderName, uid)
            if (localMsg.isCached) {
                Result.success(localMsg)
            } else {
                val folder = IMAPService.getFolderByName(folderName)
                val msg = VMailNetwork.getMsg(folder, uid)

                val attachmentList = msg.content.attachmentList
                // load and cache attachment file content, but using lazy downloading policy
                val localFileList: MutableList<LocalFile> = ArrayList<LocalFile>()
                for (attachment in attachmentList) {

                    val localFile: LocalFile = LocalFile().apply {
                        this.attachment = attachment
                        this.localMsg = localMsg
                        this.name = attachment.filename
                        this.type = attachment.type
                        this.size = attachment.size
                        this.isInline = attachment.isInline
                        this.cid = attachment.cid
                        this.path = context
                                .getExternalFilesDir("")?.absolutePath
                                .toString() + "/attachments/" + attachment.filename
                    }
                    localFileList.add(localFile)
                }
                saveAll(localFileList)
                localMsg.localFileList = localFileList

                // replace cid inline resources to local path
                val msgText: String = setCidImgToLocalPath(msg)

                val msgType = msg.content.mainBody.type
                localMsg.apply {
                    text = msgText
                    type = msgType
                    isCached = true
                }.save()
                Result.success(localMsg)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
        emit(result.getOrDefault(LocalMsg()))
    }

    /**
     * Change inline resource cid path to downloaded inline resource local storage path
     *
     * @since 1.0
     *
     * @author Mingyan Wang(Cyril)
     * @sample `val` msgText: String = setCidImgToLocalPath(msg)
     *
     * @param msg The message to replace inline resource path
     *
     * @return return the original mainBody text if it does not contain inline resource path.
     * If contains, return path replaced with local path mainBody text.
     */
    private fun setCidImgToLocalPath(msg: Message): String {
        // get text in mainBody
        val text = msg.content.mainBody.text
        val attachmentList = msg.content.attachmentList

        // get all img tag using jsoup
        val doc: Document = Jsoup.parse(text)
        val imgTags: Elements = doc.select("img[src]")
        for (i in 0 until imgTags.size) {
            val element: Element = imgTags[i]
            val src: String = element.attr("src") // get aboluste path of src
            Log.d("getCid", "before change image src: $src")
            if (src.startsWith("cid:")) {
                // start with "cid" and inline resource contained in attachment, download this resource
                // Change "cid" resource reference to local resource path in src attribute of img tag
                Log.d("getCid", "===src===$src")
                for (attachment in attachmentList) {
                    if (attachment.isInline && attachment.cid == src) {
                        attachment.download {
                            Log.d(
                                    "getCid",
                                    "downloading img......."
                            )
                        }
                        element.attr("src", "file://" + attachment.file.path)
                    }
                }
            } else {
                Log.d("getCid", "http/https image")
            }
        }
        return if (text.contains("<html>")) {
            doc.toString()
        } else {
            doc.select("body>*").toString()
        }
    }

    /**
     * Send a local draft from local storage to target address(To).
     * Method can only be called from viewModel layer.
     *
     * @since 1.0
     *
     * @author Mingyan Wang(Cyril)
     *
     * @sample sendEmail(draft)
     *
     * @param draft The draft to send
     *
     * @throws `throw` exception that cause send email failure.
     *
     * @return If send successfully, return true.
     * Otherwise, return false.
     */
    fun sendEmail(draft: Draft) = liveData(Dispatchers.IO) {
        val result = try {
            val isSendSuccess = VMailNetwork.sendEmail(draft)
            Result.success(isSendSuccess)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
        emit(result.getOrDefault(false))
    }

    /**
     * Save a local draft from local storage to server
     * Method can only be called from viewModel layer.
     *
     * @since 1.0
     *
     * @author Mingyan Wang(Cyril)
     *
     * @sample saveEmail(draft)
     *
     * @param draft The draft to send
     *
     * @throws `throw` exception that cause save email failure.
     *
     * @return If send successfully, return true.
     * Otherwise, return false.
     */
    fun saveEmail(draft: Draft) = liveData(Dispatchers.IO) {
        val result = try {
            val isSaveEmail = VMailNetwork.saveEmail(draft)
            Result.success(isSaveEmail)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure(e)
        }
        emit(result.getOrDefault(false))

    }


}