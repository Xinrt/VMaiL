package com.teamsixers.vmail.logic.dao

import org.junit.Before
import org.junit.Test

class LocalMsgDaoTest {

    @Before
    fun setUp() {
    }

    @Test
    fun saveLocalMsgList_EmptyList() {
        LocalMsgDao.saveLocalMsgList("INBOX", emptyList())
        val size = LocalMsgDao.getLocalMsgList("INBOX").size
        assert(size == 0)
    }

    @Test
    fun saveLocalMsgList() {
    }

    @Test
    fun getLocalMsgList() {
    }

    @Test
    fun deleteLocalMsgListByUid() {
    }

    @Test
    fun getLocalUidArray() {
    }

    @Test
    fun getLocalMsg() {
    }
}