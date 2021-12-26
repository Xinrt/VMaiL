package com.teamsixers.vmail.ui.compose.IdleState

import com.teamsixers.vmail.ui.compose.ComposeActivity
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Test

class AddSubjectTaskTest {

    @Test
    fun doTask() {
        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        every {
            composeActivity.textToSpeech(any())
        } just Runs
    }
}