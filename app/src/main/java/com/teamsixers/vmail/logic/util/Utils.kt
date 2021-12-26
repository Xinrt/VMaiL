package com.teamsixers.vmail.logic.util

import com.smailnet.emailkit.Message
import com.teamsixers.vmail.logic.model.LocalMsg
import java.util.*

/**
 * Utils singleton class is used for providing util function
 * for network layer class.
 *
 * All methods should run in coroutine context for efficiency.
 *
 * All network layer functions provides API for repository layer.
 */
object Utils {

    /**
     * This methods provides function for
     * converting Message instances to local messages instances that can be
     * stored in local database.
     *
     * @param msgList the messages list to be stored.
     * @param msgFolderName the folder name of messages to be stored.
     *
     * @return the local messages list to be stored.
     *
     * @author Mingyan(Cyril) Wang
     *
     * @since 1.0
     */
    fun messageListToLocalMsgList(msgList: List<Message>, msgFolderName: String): MutableList<LocalMsg> {
        val localMsgList: MutableList<LocalMsg> = ArrayList()
        for (msg in msgList) {
            var recipientAddr = ""
            var recipNickname = ""
            val recipientsList: List<Message.Recipients.To>? = msg.recipients.toList
            if (recipientsList != null && recipientsList.isNotEmpty()) {
                if (recipientsList[0].address != null) {
                    recipientAddr = recipientsList[0].address
                }
                if (recipientsList[0].nickname != null) {
                    recipNickname = recipientsList[0].nickname
                }
            }
            // populate new localMsg fields
            val localMsg = LocalMsg().apply {
                uID = msg.uid
                isRead = msg.flags.isRead
                subject = msg.subject ?: ""
                if (msg.sender != null) {
                    senderNickname = msg.sender.nickname ?: ""
                    senderAddress = msg.sender.address ?: ""
                } else {
                    senderNickname = ""
                    senderAddress = ""
                }
                recipientAddress = recipientAddr
                recipientNickname = recipNickname
                date = msg.sentDate.text ?: ""
                folderName = msgFolderName
            }
            localMsgList.add(localMsg)
        }
        return localMsgList
    }
}