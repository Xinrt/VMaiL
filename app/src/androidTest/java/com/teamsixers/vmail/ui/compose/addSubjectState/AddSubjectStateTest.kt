package com.teamsixers.vmail.ui.compose.addSubjectState

import com.teamsixers.vmail.ui.compose.ComposeActivity
import io.mockk.*
import org.junit.Test

class AddSubjectStateTest {

    @Test
    fun executeCommand_CommonInput() {
        val inputCommand = "my subject is xyz"

        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        val addSubjectState = AddSubjectState()
        every {
            composeActivity.setSubject(any())
            composeActivity.textToSpeech(any())
        } just Runs

        addSubjectState.executeCommand(inputCommand, composeActivity)
        verify {
            composeActivity.setSubject(any())
            composeActivity.textToSpeech(any())
        }
    }
    @Test
    fun executeCommand_SpecialInput() {
        val inputCommand = "my subject is @ . com"

        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        val addSubjectState = AddSubjectState()
        every {
            composeActivity.setSubject(any())
            composeActivity.textToSpeech(any())
        } just Runs

        addSubjectState.executeCommand(inputCommand, composeActivity)
        verify {
            composeActivity.setSubject(any())
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