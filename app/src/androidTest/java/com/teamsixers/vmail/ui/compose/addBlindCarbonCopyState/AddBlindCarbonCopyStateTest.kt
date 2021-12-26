package com.teamsixers.vmail.ui.compose.addBlindCarbonCopyState

import com.teamsixers.vmail.ui.compose.ComposeActivity
import io.mockk.*
import org.junit.Test

class AddBlindCarbonCopyStateTest {

    @Test
    fun executeCommand_EmailAddressInput() {
        val inputCommand = "947411254 at qq dot com"

        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        val addBlindCarbonCopyState = AddBlindCarbonCopyState()
        every {
            composeActivity.setBlindCarbonCopy(any())
            composeActivity.textToSpeech(any())
        } just Runs

        addBlindCarbonCopyState.executeCommand(inputCommand, composeActivity)
        verify {
            composeActivity.setBlindCarbonCopy(any())
            composeActivity.textToSpeech(any())
        }
    }
    @Test
    fun executeCommand_NotEmailAddressInput() {
        val inputCommand = "947411254 com"

        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        val addBlindCarbonCopyState = AddBlindCarbonCopyState()
        every {
            composeActivity.setBlindCarbonCopy(any())
            composeActivity.textToSpeech(any())
        } just Runs

        addBlindCarbonCopyState.executeCommand(inputCommand, composeActivity)
    }
    @Test
    fun onTTSDone_noInput(){
        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        every {
            composeActivity.startActiveListening(composeActivity)
        } just Runs
    }
}