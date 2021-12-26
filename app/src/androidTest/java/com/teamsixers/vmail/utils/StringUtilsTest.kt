package com.teamsixers.vmail.utils

import junit.framework.Assert.assertEquals
import org.junit.Test

class StringUtilsTest {

    @Test
    fun processRawEmailAddress() {
    }


    @Test
    fun testProcessRawEmailAddress_NoAtStringInput() {
        val testEmail = "123456qqdotcom"
        val processRawEmailAddress = StringUtils.processRawEmailAddress(testEmail)
        assertEquals(processRawEmailAddress, "123456qqdotcom")
    }

    @Test
    fun testProcessRawEmailAddress_CorrectInput() {
        val testEmail = "123456atqqdotcom"
        val processRawEmailAddress = StringUtils.processRawEmailAddress(testEmail)
        assertEquals(processRawEmailAddress, "123456@qq.com")
    }
    @Test
    fun getStringAfter() {
    }
}