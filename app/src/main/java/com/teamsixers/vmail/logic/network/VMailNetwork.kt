package com.teamsixers.vmail.logic.network

import com.smailnet.emailkit.Draft
import com.smailnet.emailkit.EmailKit
import com.smailnet.emailkit.Folder
import com.smailnet.emailkit.Message
import com.teamsixers.vmail.VMailApplication
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * VMailNetwork singleton class is used for performing
 * major tasks of VMail connecting to email server.
 *
 * All methods should run in coroutine context for efficiency.
 *
 * All network layer functions provides API for repository layer.
 */
object VMailNetwork {
    /**
     * Logger tag
     */
    val TAG: String = VMailNetwork::class.java.simpleName

    /**
     * Authentication of an email account running on coroutine context.
     *
     * @param config the configuration of current email app, which includes
     * account, password, and email type.
     *
     * @since 1.0
     *
     * @author Mingyan(Cyril) Wang
     */
    suspend fun authMailAccount(config : EmailKit.Config): Boolean {
        return suspendCoroutine {
            EmailKit.auth(config, object : EmailKit.GetAuthCallback {
                override fun onSuccess() {
                    it.resume(true)
                }

                override fun onFailure(errMsg: String) {
                    it.resumeWithException(RuntimeException(errMsg))
                }
            })
        }
    }


    /**
     * Load messages(emails) from server running on coroutine context.
     * Only load at most 20 messages from server(paging scheme).
     * All loaded messages will have UID less than lastUID().
     *
     * If the local client has not cached email messages,
     * the lastUID value is passed in a value less than 0, such as -1,
     * and it will load the latest 20 emails.
     *
     * If the local client has already cached mail messages,
     * it will pass in lastUID the value
     * with the smallest UID among all the mails cached locally,
     * and it will load 20 mails that are smaller than the lastUID value.
     * The number of mails loaded each time is between [0, 20].
     *
     * Note: In the process of loading the mail message,
     * when reading the data of the Message object,
     * it is recommended to only pull the data of the mail header information.
     * Do not read the mail body and attachments,
     * because reading these two data is using lazy loading.
     * Affect the user experience
     *
     * @param lastUID the latest message UID in local storage.
     * @param folder the folder that will receive new messages.
     *
     * @since 1.0
     *
     * @author Mingyan(Cyril) Wang
     */
    suspend fun loadMsg(lastUID: Long, folder: Folder): List<Message> {
        return suspendCoroutine {
            folder.load(lastUID, object : EmailKit.GetLoadCallback {
                override fun onSuccess(msgList: List<Message>) {
                    it.resume(msgList)
                }

                override fun onFailure(errMsg: String) {
                    it.resumeWithException(RuntimeException(errMsg))
                }
            })
        }
    }

    /**
     * The method is used for local client synchronizes mail with the mail server.
     * The synchronization is mainly to check whether the mail server has new mail
     * and whether the locally cached mail has been deleted in the server.
     *
     * @param folder the folder that will be synchronized.
     *
     * @param localUIDArray All mail UIDs that have been cached by the local client.
     *
     * @return List<Message> is the newMsgList.
     *         LongArray the UID of messages that server had deleted and should be deleted locally.
     *              This will be used for comparing local UID.
     *
     *
     * @since 1.0
     *
     * @author Mingyan(Cyril) Wang
     */
    suspend fun syncMsg(folder: Folder, localUIDArray: LongArray): Pair<List<Message>, LongArray>{
        return suspendCoroutine {
            folder.sync(localUIDArray, object : EmailKit.GetSyncCallback {
                override fun onSuccess(newMsgList: List<Message>, deletedUID: LongArray) {
                    it.resume(Pair(newMsgList, deletedUID))
                }

                override fun onFailure(errMsg: String) {
                    it.resumeWithException(RuntimeException(errMsg))
                }
            })
        }
    }

    /**
     * @param config the configuration of current email app, which includes
     * account, password, and email type.
     *
     * @return all name of folders that in the server of current email account.
     *
     * @since 1.0
     *
     * @author Mingyan(Cyril) Wang
     */
    suspend fun getDefaultFolderList(config : EmailKit.Config): List<String> {
        return suspendCoroutine {
            EmailKit.useIMAPService(config)
                .getDefaultFolderList(object : EmailKit.GetFolderListCallback {
                    override fun onSuccess(folderList: MutableList<String>) {
                        it.resume(folderList)
                    }

                    override fun onFailure(errMsg: String?) {
                        it.resumeWithException(RuntimeException(errMsg))
                    }
                })
        }
    }

    /**
     * get an message from server in the context of coroutine.
     * Method can only called from Repository layer.
     *
     * @param uid The unique uid of message in a folder.
     * @param folder The folder in which message to get belongs to.
     *
     * @author Mingyan Wang(Cyril)
     *
     * @since 1.0
     *
     * @return return the retrieved email message if email is retrieved successfully.
     * If failure, resume with exception.
     */
    suspend fun getMsg(folder: Folder, uid: Long): Message {
        return suspendCoroutine {
            folder.getMsg(uid, object : EmailKit.GetMsgCallback {
                override fun onSuccess(msg: Message) {
                    it.resume(msg)
                }

                override fun onFailure(errMsg: String?) {
                    it.resumeWithException(RuntimeException(errMsg))
                }
            })
        }
    }

    /**
     * Send an email in the context of coroutine.
     * Method can only called from Repository layer.
     *
     * @param draft Draft to send
     *
     * @author Mingyan Wang(Cyril)
     *
     * @since 1.0
     *
     * @return return true if email is sent successfully.
     * If failure, resume with exception.
     */
    suspend fun sendEmail(draft: Draft): Boolean {
        return suspendCoroutine {
            EmailKit.useSMTPService(VMailApplication.config)
                .send(draft, object : EmailKit.GetSendCallback {
                    override fun onSuccess() {
                        it.resume(true)
                    }

                    override fun onFailure(errMsg: String?) {
                        it.resumeWithException(RuntimeException(errMsg))
                    }
                })

        }
    }

    /**
     * save an email in the context of coroutine.
     * Method can only called from Repository layer.
     *
     * @param draft Draft to save
     *
     * @author Mingyan Wang(Cyril)
     *
     * @since 1.0
     *
     * @return return true if email is saved successfully.
     * If failure, resume with exception.
     */
    suspend fun saveEmail(draft: Draft): Boolean {
        return suspendCoroutine {
            EmailKit.useIMAPService(VMailApplication.config)
                .draftBox
                .saveMsg(draft, object : EmailKit.GetOperateCallback {
                    override fun onSuccess() {
                        it.resume(true)
                    }

                    override fun onFailure(errMsg: String?) {
                        it.resumeWithException(RuntimeException(errMsg))
                    }
                })
        }

    }


}