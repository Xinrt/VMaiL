package com.teamsixers.vmail.ui.compose.deleteAllState

import com.teamsixers.vmail.ui.compose.ComposeActivity
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import org.junit.Test

class DeleteAllStateTest {


    @Test
    fun onTTSDone_noInput() {
        val composeActivity = mockk<ComposeActivity>(relaxed = true)
        every {
            composeActivity.startActiveListening(composeActivity)
        } just Runs
    }

    @Test
    fun deleteAll() {

    }
}
