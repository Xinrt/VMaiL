package com.teamsixers.vmail.ui.compose.readAllState

import com.teamsixers.vmail.ui.compose.ComposeActivity
import io.mockk.*
import org.junit.Test

class ReadAllStateTest {

    @Test
    fun executeCommand() {
        val inputCommand = "read all"

        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        val readAllState = ReadAllState()
        every {
            composeActivity.textToSpeech(any())
        } just Runs

        readAllState.executeCommand(inputCommand, composeActivity)
        verify {
            composeActivity.textToSpeech(any())
        }
    }

    @Test
    fun onTTSDone() {
    }
}