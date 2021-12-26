package com.teamsixers.vmail.logic.model

import com.smailnet.emailkit.Message
import org.litepal.crud.LitePalSupport


/**
 * Object relational mapping for LocalFile
 * to store attachment of an email.
 */
class LocalFile : LitePalSupport() {
    /**
     * If current local file is an inline resource of an email,
     * it should have cid. Otherwise, it will be null.
     */
    var cid: String? = null

    /**
     * Indication of current local file is an inline resource or not.
     */
    var isInline: Boolean = false

    /**
     * Size of local file.
     */
    var size = 0

    /**
     * Name of local file.
     */
    var name: String? = null

    /**
     * Type of local file.
     */
    var type: String? = null

    /**
     * File path of local file.
     */
    var path: String? = null

    /**
     * The local message where local file belongs to
     */
    var localMsg = LocalMsg()

    /**
     * The related attachment object
     * Used for setting attachment link to lazy downloading.
     */
    var attachment: Message.Content.Attachment? = null
}

