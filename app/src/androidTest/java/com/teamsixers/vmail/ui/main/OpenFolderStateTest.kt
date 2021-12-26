package com.teamsixers.vmail.ui.main

import androidx.test.ext.junit.runners.AndroidJUnit4
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class OpenFolderStateTest {

    @Test
    fun executeCommand_InputInbox_ShouldMatchFolderInFolderList() {
        val folderList = arrayListOf("INBOX")
        val command = "inbox"

        val findResult = FuzzySearch.extractOne(command, folderList)
        assert(findResult.score > 85)
        assert(findResult.string == "INBOX")
    }

    @Test
    fun executeCommand_InputHelloBox_ShouldMatchFolderInFolderList() {
        val folderList = arrayListOf("INBOX")
        val command = "hello box"

        val findResult = FuzzySearch.extractOne(command, folderList)
        assert(findResult.score < 85)
    }
    @Test
    fun onTTSStart() {
    }

    @Test
    fun onTTSDone() {
    }

    @Test
    fun onTTSError() {
    }
}