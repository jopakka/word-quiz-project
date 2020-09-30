/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.database.user

import org.junit.Test

import org.junit.Assert.*

class UserTest {
    private val user = User("finnish", "english", 12, 5)

    @Test
    fun getFakeLanguage() {
        assertNotEquals("swedish", user.language)
    }

    @Test
    fun getLanguage() {
        assertEquals("finnish", user.language)
    }

    @Test
    fun getFakeAnswerLanguage() {
        assertNotEquals("swedish", user.answerLanguage)
    }

    @Test
    fun getAnswerLanguage() {
        assertEquals("english", user.answerLanguage)
    }

    @Test
    fun getFakeTotalGuesses() {
        assertNotEquals(12313, user.totalGuesses)
    }

    @Test
    fun getTotalGuesses() {
        assertEquals(12, user.totalGuesses)
    }

    @Test
    fun getFakeRightGuesses() {
        assertNotEquals(123213, user.rightGuesses)
    }

    @Test
    fun getRightGuesses() {
        assertEquals(5, user.rightGuesses)
    }
}