package com.teamsixers.vmail.ui.compose.addreceiverstate

import com.teamsixers.vmail.ui.compose.ComposeActivity
import io.mockk.*
import org.junit.Test

class AddReceiverStateTest {

    @Test
    fun executeCommand_EmailAddressInput() {
        val inputCommand = "947411254 at qq dot com"

        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        val addReceiverState = AddReceiverState()
        every {
            composeActivity.setReceiver(any())
            composeActivity.textToSpeech(any())
        } just Runs

        addReceiverState.executeCommand(inputCommand, composeActivity)
        verify {
            composeActivity.setReceiver(any())
            composeActivity.textToSpeech( any())
        }
    }

    @Test
    fun executeCommand_NotEmailAddressInput() {
        val inputCommand = "947411254 com"

        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        val addReceiverState = AddReceiverState()
        every {
            composeActivity.setReceiver(any())
            composeActivity.textToSpeech(any())
        } just Runs

        addReceiverState.executeCommand(inputCommand, composeActivity)
    }
}