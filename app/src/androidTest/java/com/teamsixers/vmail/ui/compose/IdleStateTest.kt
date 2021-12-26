@file:Suppress("UNCHECKED_CAST")

package com.teamsixers.vmail.ui.compose

import com.teamsixers.vmail.ui.Task
import com.teamsixers.vmail.ui.compose.IdleState.*
import com.teamsixers.vmail.ui.compose.IdleState.AddContentTask
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.junit.Assert.assertNotNull
import org.junit.Test

class IdleStateTest {

    private val cmdTaskMap by lazy {
        hashMapOf<String, Task>(
                "send email" to SendEmailTask(),
                "back" to BackTask(),
                "add carbon copy" to AddCarbonCopyTask(),
                "add blind carbon copy" to AddBlindCarbonCopyTask(),
                "add receiver" to AddReceiverTask(),
                "add subject" to AddSubjectTask(),
                "add content" to AddContentTask(),
                "delete word" to DeleteWordTask(),
                "delete sentence" to DeleteSentenceTask(),
                "read all" to ReadAllTask(),
                "delete all" to DeleteAllTask(),
                "replace word" to ReplaceTask()
        )
    }
    @Test
    fun testExecuteCommand_SendEmail() {
        val findResult = FuzzySearch.extractOne("Send email", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "send email")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == SendEmailTask::class.java)
    }

    @Test
    fun testExecuteCommand_SendMyEmail() {
        val findResult = FuzzySearch.extractOne("Send my email", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "send email")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == SendEmailTask::class.java)
    }

    @Test
    fun testExecuteCommand_SendTheEmail() {
        val findResult = FuzzySearch.extractOne("Send the email", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "send email")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == SendEmailTask::class.java)
    }

    @Test
    fun testExecuteCommand_SendThisEmail() {
        val findResult = FuzzySearch.extractOne("Send this email", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "send email")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == SendEmailTask::class.java)
    }

    @Test
    fun testExecuteCommand_SendMyEmailLong() {
        val findResult = FuzzySearch.extractOne("Send my email, thanks", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "send email")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == SendEmailTask::class.java)
    }

    @Test
    fun testExecuteCommand_Back() {
        val findResult = FuzzySearch.extractOne("Back", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "back")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == BackTask::class.java)
    }

    @Test
    fun testExecuteCommand_AddCarbonCopy() {
        val findResult = FuzzySearch.extractOne("Add carbon copy", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "add carbon copy")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == AddCarbonCopyTask::class.java)
    }

    @Test
    fun testExecuteCommand_AddBlindCarbonCopy() {
        val findResult = FuzzySearch.extractOne("Add blind carbon copy", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "add blind carbon copy")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == AddBlindCarbonCopyTask::class.java)
    }

    @Test
    fun testExecuteCommand_AddReceiver() {
        val findResult = FuzzySearch.extractOne("add receiver", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "add receiver")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == AddReceiverTask::class.java)
    }

    @Test
    fun testExecuteCommand_AddContent() {
        val findResult = FuzzySearch.extractOne("add content", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "add content")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == AddContentTask::class.java)
    }

    @Test
    fun testExecuteCommand_DeleteWord() {
        val findResult = FuzzySearch.extractOne("delete word", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "delete word")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == DeleteWordTask::class.java)
    }

    @Test
    fun testExecuteCommand_DeleteWorldInput() {
        val findResult = FuzzySearch.extractOne("delete world", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "delete word")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == DeleteWordTask::class.java)
    }
    @Test
    fun testExecuteCommand_DeleteSentence() {
        val findResult = FuzzySearch.extractOne("Delete sentence", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "delete sentence")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == DeleteSentenceTask::class.java)
    }

    @Test
    fun testExecuteCommand_DeleteAll() {
        val findResult = FuzzySearch.extractOne("Delete all", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "delete all")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == DeleteAllTask::class.java)
    }

    @Test
    fun testExecuteCommand_Replace() {
        val findResult = FuzzySearch.extractOne("Replace", cmdTaskMap.keys)
        assert(findResult.score > 85)
        assert(findResult.string == "replace")
        val task = cmdTaskMap[findResult.string]
        assertNotNull(task)
        assert(task!!::class.java == ReplaceTask::class.java)
    }


//
//    private fun getMatchedCommandMethod(): Pair<IdleState, Method> {
//        val idleState = IdleState()
//        val getMatchedCommandMethod =
//            IdleState::class.java.getDeclaredMethod("getMatchedCommand", String::class.java)
//        getMatchedCommandMethod.isAccessible = true
//        return Pair(idleState, getMatchedCommandMethod)
//    }
//

    @Test
    fun executeCommand() {
        val cmdList = arrayListOf("send email", "back", "subject is")
        assert(cmdList.size == 3)
    }
}