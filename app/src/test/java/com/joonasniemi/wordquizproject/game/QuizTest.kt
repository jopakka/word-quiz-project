/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.game

import com.joonasniemi.wordquizproject.network.Word
import io.mockk.impl.annotations.MockK
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import java.util.*

class QuizTest {
    @MockK
    private lateinit var quiz: Quiz

    private val words = listOf(
        Word(1, "well", lang = "english", translationIds = setOf(1,2)),
        Word(2, "dog", lang = "english", translationIds = setOf(3,4)),
        Word(3, "cat", lang = "english", translationIds = setOf(5,6))
    )

    @Before
    fun setUp(){
        quiz = Quiz
        quiz.initGame(words, "finnish")
        quiz.setQuestion()
    }

    @Test
    fun getMaxQuestions() {
        assertEquals(3, Quiz.maxQuestions)
    }

    @Test
    fun getAnswerLanguage() {
        assertEquals("finnish", Quiz.answerLanguage)
    }

    @Test
    fun getLanguage() {
        assertEquals("english", Quiz.language)
    }

    @Test
    fun getAnswers() {
        assertEquals(4, Quiz.answers.value?.size)
    }

    @Test
    fun getCurrentWord() {
    }

    @Test
    fun getUserCorrectAnswers() {
    }

    @Test
    fun getStarted() {
    }

    @Test
    fun resetGame() {
    }

    @Test
    fun initGame() {
    }

    @Test
    fun setQuestion() {
    }

    @Test
    fun nextQuestion() {
    }

    @Test
    fun checkAnswer() {
    }
}