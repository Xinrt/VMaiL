package com.teamsixers.vmail.logic.network

import com.smailnet.emailkit.EmailKit
import com.teamsixers.vmail.VMailApplication

/**
 * Provide IMAP service for Repository layer.
 */
object IMAPService {

    /**
     * getFolderByName is used for getting folder by name from server.
     *
     * @param folderName the name of folder to be retrieved.
     *
     * @return the folder object of corresponding folder name.
     *
     * @since 1.0
     *
     * @author Mingyan(Cyril) Wang
     *
     */
    fun getFolderByName(folderName: String) = EmailKit.useIMAPService(VMailApplication.config).getFolder(folderName)

}