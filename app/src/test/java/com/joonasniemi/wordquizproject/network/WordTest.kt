/*
 * Joonas Niemi
 * 1908997
 */

package com.joonasniemi.wordquizproject.network

import org.junit.Test

import org.junit.Assert.*

class WordTest {
    @Test
    fun create() {
        val w = Word(0, "kaivo", lang = "finnish", translationIds = setOf())
        assertEquals(w.text, "kaivo")
        assertEquals(w.lang, "finnish")
        assertEquals(0, w.translationCount("english"))
    }

    @Test
    fun translationsEqual() {
        val w = Word(1, "kissa", lang = "finnish", translationIds = setOf())
        val w2 = Word(2, "cat", lang = "english", translationIds = setOf())
        w.addTranslation(w2)
        assertEquals(true, w.isTranslation(w2))
    }

    @Test
    fun addTranslationDifferentLanguage() {
        val w = Word(3, "kello", lang = "clock", translationIds = setOf())
        val w2 = Word(2, "cat", lang = "english", translationIds = setOf())
        w.addTranslation(w2)
        assertEquals(false, w.isTranslation(Word(3, "kat", lang = "swedish", translationIds = setOf())))
    }

    @Test
    fun addTranslationsNonEmpty() {
        val w = Word(4, "well", lang = "english", translationIds = setOf())
        val w2 = Word(14, "kaivo", lang = "finnish", translationIds = setOf())
        w.addTranslations(setOf(w2))
        assertEquals(true, w.isTranslation(w2))
    }

    @Test
    fun addTranslationsEmpty() {
        val w = Word(5, "well", lang = "english", translationIds = setOf())
        w.addTranslations(setOf())
        assertEquals(false, w.isTranslation(Word(1, "kaivo", lang = "finnish", translationIds = setOf())))
    }

    @Test
    fun translationCountOne() {
        val w = Word(4, "well", lang = "english", translationIds = setOf())
        val w2 = Word(14, "kaivo", lang = "finnish", translationIds = setOf())
        w.addTranslations(setOf(w2))
        assertEquals(1, w.translationCount("finnish"))
    }

    @Test
    fun translationCountZero() {
        val w = Word(4, "well", lang = "english", translationIds = setOf())
        val w2 = Word(14, "kaivo", lang = "finnish", translationIds = setOf())
        w.addTranslations(setOf(w2))
        assertEquals(0, w.translationCount("swedish"))
    }

    @Test
    fun editDistanceZero() {
        val w = Word(16, "koira", lang = "finnish", translationIds = setOf())
        assertEquals(0, w.editDistance("koira"))
    }

    @Test
    fun editDistanceOne() {
        val w = Word(52, "clock", lang = "english", translationIds = setOf())
        assertEquals(1, w.editDistance("clack"))
    }

    @Test
    fun editDistanceMany() {
        val w = Word(40, "piha", lang = "finnish", translationIds = setOf())
        assertEquals(4, w.editDistance("pullo"))
    }
}