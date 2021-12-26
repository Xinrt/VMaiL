package com.teamsixers.vmail.logic.dao

import com.smailnet.emailkit.Message
import com.teamsixers.vmail.logic.model.LocalMsg
import com.teamsixers.vmail.logic.util.Utils.messageListToLocalMsgList
import org.litepal.LitePal.where
import org.litepal.extension.find
import org.litepal.extension.saveAll

/**
 * Local message data access object.
 * This singleton class provides functions to save local messages list
 * get local messages list, get local messages uid array and get single local message.
 *
 */
object LocalMsgDao {

    /**
     * Save List of Messages to Local SQLite Database
     * All messages should belong to the same folder
     *
     * Caller should check validation of recipient address
     * NickName could be empty
     *
     * @author Mingyan Wang
     *
     * @since 1.0
     *
     * @param msgList List holds all messages to store
     * @param msgFolderName The folder that all messages belong to
     *
     * @return True if all messages in msgList are saved. False none message in msgList is saved.
     * There won't be partial saved condition.
     */
    fun saveLocalMsgList(
            msgFolderName: String,
            msgList: List<Message>
    ) : Boolean {
        val localMsgList: MutableList<LocalMsg> = messageListToLocalMsgList(msgList, msgFolderName)
        return localMsgList.saveAll()
    }




    /**
     * Get local message sorted by uid in a folder from SQLite DataBase.
     *
     * Folder name should be valid in this email server.
     *
     * @author Mingyan Wang
     *
     * @since 1.0
     *
     * @param folderName The folder to get local messages
     *
     * @return all local messages sorted by uid from a folder
     *
     */
    fun getLocalMsgList(folderName: String): MutableList<LocalMsg> {
        return where("folderName = ?", folderName)
                .find<LocalMsg>()
                .sorted().toMutableList()
    }



    /**
     * Get all local message uids from SQLite DataBase.
     *
     * Folder name should be valid in this email server.
     *
     * Please refer to JavaMail API for UID to see
     * the meaning of UID.
     *
     * @author Mingyan Wang
     *
     * @since 1.0
     *
     * @param folderName The folder to get all uids
     *
     * @return all uids from a folder in local database
     */
    fun getLocalUidArray(folderName: String): LongArray {
        return where("folderName = ?", folderName)
            .find<LocalMsg>()
            .sorted()
            .map(LocalMsg::uID).toLongArray()
    }

    /**
     * Get all local message based on a uid from SQLite DataBase.
     *
     * Folder name should be valid in this email server
     *
     * @author Mingyan Wang
     *
     * @since 1.0
     *
     * @param folderName The folder that message is in
     *
     * @param uid The uid that message has
     *
     * @return a local message from a folder or an empty list if uid message not exist in folder
     */
    fun getLocalMsg(folderName: String, uid: Long): LocalMsg {
        return where("folderName = ? and uid = ?",
                folderName, uid.toString())
                .find<LocalMsg>(true)[0]
    }
}