package com.teamsixers.vmail.logic.model

import org.litepal.crud.LitePalSupport
import java.util.*

/**
 * Object relational mapping to storing an LocalMsg
 */
class LocalMsg : LitePalSupport(), Comparable<LocalMsg> {
    /**
     * Unique identifier of an email.
     */
    var uID: Long = 0

    /**
     * Email read or not.
     */
    var isRead = false

    /**
     * Email body content and attachment cached or not.
     */
    var isCached = false

    /**
     * The subject of this email.
     */
    var subject: String = ""

    /**
     * The senderAddress of this email.
     */
    var senderAddress: String = ""

    /**
     * The senderNickname of this email.
     */
    var senderNickname: String = ""

    /**
     * The recipientAddress of this email.
     */
    var recipientAddress: String = ""

    /**
     * The recipientNickname of this email.
     */
    var recipientNickname: String = ""

    /**
     * The date of this email received.
     */
    var date: String = ""

    /**
     * The folder this email belongs to.
     */
    var folderName: String = ""

    /**
     * The mainBody of this email.
     */
    var text: String = ""

    /**
     * The type of mainBody
     * text/html, text/plain
     */
    var type: String = ""

    /**
     * The attachmentList of this email.
     */
    var localFileList: List<LocalFile> = ArrayList<LocalFile>()

    /**
     * Compare message uid.
     */
    override fun compareTo(other: LocalMsg): Int {
        return (other.uID - this.uID).toInt()
    }


}