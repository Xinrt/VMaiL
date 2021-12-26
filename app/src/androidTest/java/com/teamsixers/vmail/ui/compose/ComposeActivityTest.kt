package com.teamsixers.vmail.ui.compose

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.ActivityScenario.launch
import com.teamsixers.vmail.R
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import com.teamsixers.vmail.databinding.ActivityComposeBinding
import com.teamsixers.vmail.ui.compose.IdleState.IdleState
import io.mockk.verify
import me.xdrop.fuzzywuzzy.FuzzySearch
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class ComposeActivityTest {

    lateinit var binding: ActivityComposeBinding


    @get:Rule
    var activityScenarioRule = activityScenarioRule<ComposeActivity>()

//    @Test
//    fun useAppContext() {
//        // Context of the app under test.
//        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
//        Assert.assertEquals("com.teamsixers.vmail", appContext.packageName)
//    }
//
//    @Test
//    fun textViewTest() {
//        val scenario = launch(ComposeActivity::class.java)
//        scenario.moveToState(Lifecycle.State.CREATED)
//        onView(withId(R.id.activity_compose_bcc_tv)).check(matches(withText("bcc")));
//    }


    @Test
    fun testFuzzyMatch() {
//        val tokenSortPartialRatio =
//            tokenSortPartialRatio("send my email", "send email") // 70
//            FuzzySearch.tokenSortRatio("send the email", "send email") // 83
//        FuzzySearch.ratio("send my email", "send email") // 87
//        FuzzySearch.partialRatio("send my email", "send email") // 70
//        FuzzySearch.weightedRatio("send my email", "send email") // 95
//        FuzzySearch.tokenSetPartialRatio("send the email", "send email") // 100
//        FuzzySearch.tokenSetRatio("send the email", "send email") // 100
//        FuzzySearch.tokenSetRatio("email send", "send email") // 100
//        FuzzySearch.tokenSetRatio("I want to send my email", "send email") // 100
//        FuzzySearch.tokenSetRatio("send mail", "send email") // 100
//            FuzzySearch.ratio("add receiver", "receiver")
//        FuzzySearch.tokenSortRatio("add carbon copy", "add blind carbon copy")
//        FuzzySearch.tokenSetPartialRatio("send email", "send this email")
//        FuzzySearch.tokenSortPartialRatio("add carbon copy", "add blind carbon copy")
        val array = mutableListOf<String>("add carbon copy", "add blind carbon copy")
        val findResult = FuzzySearch.extractOne("add carbon copy", array)
        Assert.assertEquals("add carbon copy", findResult.string)
        Assert.assertEquals(100, findResult.score)
        val array1 = mutableListOf<String>("send email")
        val findResult1 = FuzzySearch.extractOne("send this email, thanks", array1)
        Assert.assertEquals("send email", findResult1.string)
        Assert.assertEquals(100, findResult1.score)

        val array2 = mutableListOf<String>("delete word")
        val findResult2 = FuzzySearch.extractOne("delete world", array2)
        Assert.assertEquals("delete word", findResult2.string)
        Assert.assertEquals(100, findResult2.score)
    }

    @Test
    fun onCreate_NormalStart() {

    }

    @Test
    fun onTTSStart_NormalCase() {

        val scenario = activityScenarioRule.scenario
        scenario.onActivity { composeActivity ->

            val state = IdleState()
            composeActivity.state = state
            composeActivity.onTTSStart()
            verify {
                state.onTTSStart(composeActivity)
            }
        }

    }

    @Test
    fun onTTSDone() {
    }

    @Test
    fun onTTSError() {
    }

    @Test
    fun onAsrError() {
    }

    @Test
    fun onAsrSuccess() {
    }

    @Test
    fun setAsrParameter() {
    }

    @Test
    fun checkForm() {
    }

    @Test
    fun setReceiver() {
    }
}