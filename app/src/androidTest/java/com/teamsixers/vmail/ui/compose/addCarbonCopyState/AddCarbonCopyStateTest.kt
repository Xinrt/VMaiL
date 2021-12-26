package com.teamsixers.vmail.ui.compose.addCarbonCopyState

import com.teamsixers.vmail.ui.compose.ComposeActivity
import io.mockk.*
import org.junit.Test

class AddCarbonCopyStateTest {

    @Test
    fun executeCommand_EmailAddressInput() {
        val inputCommand = "1399734500 at qq dot com"

        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        val addCarbonCopyState = AddCarbonCopyState()
        every {
            composeActivity.setCarbonCopy(any())
            composeActivity.textToSpeech(any())
        } just Runs

        addCarbonCopyState.executeCommand(inputCommand, composeActivity)
        verify {
            composeActivity.setCarbonCopy(any())
            composeActivity.textToSpeech(any())
        }
    }
    @Test
    fun executeCommand_NotEmailAddressInput() {
        val inputCommand = "947411254 com"

        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        val addCarbonCopyState = AddCarbonCopyState()
        every {
            composeActivity.setCarbonCopy(any())
            composeActivity.textToSpeech(any())
        } just Runs

        addCarbonCopyState.executeCommand(inputCommand, composeActivity)
    }
    @Test
    fun onTTSDone_noInput(){
        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        every {
            composeActivity.startActiveListening(composeActivity)
        } just Runs
    }
}