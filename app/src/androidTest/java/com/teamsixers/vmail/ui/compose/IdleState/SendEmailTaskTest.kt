package com.teamsixers.vmail.ui.compose.IdleState

import com.teamsixers.vmail.ui.compose.ComposeActivity
import com.teamsixers.vmail.ui.detail.DetailActivity
import io.mockk.*
import org.junit.Assert.assertThrows
import org.junit.Test

class SendEmailTaskTest {

    /**
     * Mockk should work with API 28 version.
     */
    @Test
    fun doTask_correctInput() {
        val composeActivity = mockk<ComposeActivity>()
        val sendEmailTask = SendEmailTask()
        every { composeActivity.checkForm() } just Runs

        sendEmailTask.doTask(composeActivity)
        verify { composeActivity.checkForm() }
    }


    @Test
    fun doTask_uncorrectedInput() {
        val sendEmailTask = SendEmailTask()
        assertThrows(RuntimeException::class.java) {
            sendEmailTask.doTask(DetailActivity())
        }
    }
}