package com.teamsixers.vmail.ui

import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BaseActivityTest {


    @get:Rule
    var rule = activityScenarioRule<BaseActivity>()



    @Test
    fun testTextToSpeech_NonEmptyInput() {
        val scenario = rule.scenario
        val inputText = "welcome"
        scenario.onActivity { activity ->
        }

    }
}