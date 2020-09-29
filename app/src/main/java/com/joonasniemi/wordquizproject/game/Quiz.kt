/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.game

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joonasniemi.wordquizproject.network.Word

object Quiz {
    private const val TAG = "Quiz"

    private val words = mutableListOf<Word>()
    val maxQuestions: Int
        get() = words.size

    lateinit var answerLanguage: String
        private set

    lateinit var language: String
        private set

    private val ANSWERS = MutableLiveData<List<String>>()
    val answers: LiveData<List<String>>
        get() = ANSWERS

    private val CURRENT_WORD = MutableLiveData<Word>()
    val currentWord: LiveData<Word>
        get() = CURRENT_WORD

    var questionIndex = 0
    val userCorrectAnswers = mutableListOf<Word>()
    private var STARTED: Boolean = false
    val started: Boolean
        get() = STARTED

    fun resetGame() {
        words.clear()
        answerLanguage = ""
        language = ""
        ANSWERS.value = null
        CURRENT_WORD.value = null
        questionIndex = 0
        userCorrectAnswers.clear()
        STARTED = false
    }

    fun initGame(list: List<Word>, answerLanguage: String) {
        if (started) return
        words.addAll(list)
        this.answerLanguage = answerLanguage
        language = list.first().lang
        STARTED = true
    }

    fun setQuestion() {
        CURRENT_WORD.value = words[questionIndex]
        setAnswers()
    }

    private fun setAnswers() {
        val correct = currentWord.value?.translations?.first { it.lang == answerLanguage }
        Log.i(TAG, correct.toString())
        correct?.let { corr ->
            val mutList: MutableList<String> = mutableListOf()
            mutList.add(corr.text)
            while (mutList.size < 4) {
                val newAnswer = corr.text.replaceFirst("aeiouyäöå".random(), "aeiouy".random(), true)
                if (!mutList.contains(newAnswer))
                    mutList.add(newAnswer)
            }
            ANSWERS.value = mutList.shuffled()
        }
    }

    fun nextQuestion(): Boolean {
        questionIndex++
        return questionIndex < words.size
    }

    fun checkAnswer(answerIndex: Int): Boolean {
        if (checkDistance(answers.value?.get(answerIndex), answerLanguage) == 0) {
            currentWord.value?.let {
                userCorrectAnswers.add(it)
                return true
            }
        }
        return false
    }

    private fun checkDistance(answer: String?, language: String): Int? {
        return currentWord.value?.translations
            ?.first { it.lang == language }
            ?.editDistance(answer)
    }
}