/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joonasniemi.wordquizproject.network.Word

/**
 * Quiz game singleton
 */
object Quiz {

    /**
     * List of current words for game
     */
    private val words = mutableListOf<Word>()

    /**
     * Size of [words] list
     */
    val maxQuestions: Int
        get() = words.size

    /**
     * Games current answerLanguage
     */
    lateinit var answerLanguage: String
        private set

    /**
     * Games current language
     */
    lateinit var language: String
        private set

    /**
     * MutableLivaData list of current word answers
     */
    private val ANSWERS = MutableLiveData<List<String>>()

    /**
     * Gets [ANSWERS] mutableLiveData list and cast it to LiveData
     */
    val answers: LiveData<List<String>>
        get() = ANSWERS

    /**
     * MutableLivaData list of current word answers
     */
    private val CURRENT_WORD = MutableLiveData<Word>()

    /**
     * Gets [CURRENT_WORD] mutableLiveData and cast it to LiveData
     */
    val currentWord: LiveData<Word>
        get() = CURRENT_WORD

    /**
     * Games current questions id
     */
    private var questionIndex = 0

    /**
     * List of users correct answers
     */
    val userCorrectAnswers = mutableListOf<Word>()

    /**
     * Boolean that tells is game running
     * Can be set only in this object
     */
    var started: Boolean = false
        private set

    /**
     * Function that reset all variables to default state
     */
    fun resetGame() {
        words.clear()
        answerLanguage = ""
        language = ""
        ANSWERS.value = null
        CURRENT_WORD.value = null
        questionIndex = 0
        userCorrectAnswers.clear()
        started = false
    }

    /**
     * Function that init variables for game
     * except game is all ready running
     */
    fun initGame(list: List<Word>, answerLanguage: String) {
        if (started) return
        words.addAll(list)
        this.answerLanguage = answerLanguage
        language = list.first().lang
        started = true
    }

    /**
     * Sets new current word value and answer for that
     */
    fun setQuestion() {
        CURRENT_WORD.value = words[questionIndex]
        setAnswers()
    }

    /**
     * Sets answers for current word
     */
    private fun setAnswers() {
        val correct = currentWord.value?.translations?.first { it.lang == answerLanguage }
        correct?.let { corr ->
            val mutList: MutableList<String> = mutableListOf()
            mutList.add(corr.text)

            // Add 3 fake answers to list by selecting random vowel and replacing
            // it with another random vowel
            while (mutList.size < 4) {
                val newAnswer = corr.text.replaceFirst("aeiouyäöå".random(), "aeiouy".random(), true)
                if (!mutList.contains(newAnswer))
                    mutList.add(newAnswer)
            }
            ANSWERS.value = mutList.shuffled()
        }
    }

    /**
     * Updates [questionIndex] value by 1 and return true if [questionIndex]
     * is less than [words] list size
     */
    fun nextQuestion(): Boolean {
        questionIndex++
        return questionIndex < words.size
    }

    /**
     * Uses [checkDistance] function to check if answers distance is 0
     * returns true if it is
     */
    fun checkAnswer(answerIndex: Int): Boolean {
        if (checkDistance(answers.value?.get(answerIndex), answerLanguage) == 0) {
            currentWord.value?.let {
                userCorrectAnswers.add(it)
                return true
            }
        }
        return false
    }

    /**
     * Takes current words right answer and return editDistance of [answer]
     * or null if there is error
     */
    private fun checkDistance(answer: String?, language: String): Int? {
        return currentWord.value?.translations
            ?.first { it.lang == language }
            ?.editDistance(answer)
    }
}