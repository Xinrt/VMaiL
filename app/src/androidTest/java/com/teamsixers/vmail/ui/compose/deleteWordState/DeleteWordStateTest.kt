package com.teamsixers.vmail.ui.compose.deleteWordState

import com.teamsixers.vmail.ui.compose.ComposeActivity
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Test

class DeleteWordStateTest {


    @Test
    fun onTTSDone() {
        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        every {
            composeActivity.startActiveListening(composeActivity)
        } just Runs
    }
}