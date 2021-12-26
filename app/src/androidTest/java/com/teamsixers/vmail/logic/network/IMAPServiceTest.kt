package com.teamsixers.vmail.logic.network

import com.smailnet.emailkit.EmailKit
import com.teamsixers.vmail.VMailApplication
import org.junit.Test

class IMAPServiceTest {

    @Test
    fun getFolderByName_SameFolder() {
        VMailApplication.config = EmailKit.Config()
        val folderByName = IMAPService.getFolderByName("INBOX")
        val folder = EmailKit.useIMAPService(VMailApplication.config).getFolder("INBOX")
        assert(folder.folderName == folderByName.folderName)
    }
}