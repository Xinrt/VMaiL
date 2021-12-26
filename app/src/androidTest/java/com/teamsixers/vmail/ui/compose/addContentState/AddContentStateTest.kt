package com.teamsixers.vmail.ui.compose.addContentState

import com.teamsixers.vmail.ui.compose.ComposeActivity
import io.mockk.*
import org.junit.Test

class AddContentStateTest {

    @Test
    fun executeCommand_ContentInput(){
        val inputCommand = " This is a boy"

        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        val addContentState = AddContentState()
        every {
            composeActivity.setContent(any())
            composeActivity.textToSpeech( any())
        } just Runs

        addContentState.executeCommand(inputCommand, composeActivity)
        verify {
            composeActivity.setContent(any())
            composeActivity.textToSpeech(any())
        }
    }
    @Test
    fun executeCommand_ContentInputQuestion(){
        val inputCommand = " Where is the boy"

        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        val addContentState = AddContentState()
        every {
            composeActivity.setContent(any())
            composeActivity.textToSpeech( any())
        } just Runs

        addContentState.executeCommand(inputCommand, composeActivity)
        verify {
            composeActivity.setContent(any())
            composeActivity.textToSpeech(any())
        }
    }

    @Test
    fun onTTSDone_noInput(){
        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        every {
            composeActivity.startActiveListening(composeActivity)
        } just Runs
    }
}