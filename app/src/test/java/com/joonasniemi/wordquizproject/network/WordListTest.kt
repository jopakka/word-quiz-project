package com.joonasniemi.wordquizproject.network

import org.junit.Test

import org.junit.Assert.*

class WordListTest {
    private val listOfWords = listOf(Word(1, "kissa", lang = "finnish", translationIds = setOf()))
    private val list = WordList(listOfWords, "english")

    @Test
    fun getList() {
        assertEquals(listOfWords, list.list)
    }

    @Test
    fun getTranslationLanguage() {
        assertEquals("english", list.translationLanguage)
    }
}