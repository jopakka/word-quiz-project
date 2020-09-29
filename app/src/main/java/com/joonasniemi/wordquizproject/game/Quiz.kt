package com.joonasniemi.wordquizproject.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.joonasniemi.wordquizproject.network.Word

class Quiz {

    fun destroy() {
        INSTANCE = null
    }

    companion object {
        private var INSTANCE: Quiz? = null
        val instance: Quiz
            get() {
                if(INSTANCE == null)
                    INSTANCE = Quiz()
                return INSTANCE!!
            }
    }

    private val words = mutableListOf<Word>()
    val maxQuestions: Int
        get() = words.size

    lateinit var answerLanguage: String
        private set

    lateinit var language: String
        private set

    private val _answers = MutableLiveData<List<String>>()
    val answers: LiveData<List<String>>
        get() = _answers

    private val _currentWord = MutableLiveData<Word>()
    val currentWord: LiveData<Word>
        get() = _currentWord

    var questionIndex = 0
    val userCorrectAnswers = mutableListOf<Word>()

    fun initGame(list: List<Word>, answerLanguage: String){
        words.addAll(list)
        this.answerLanguage = answerLanguage
        language = list.first().lang
    }

    fun setQuestion() {
        _currentWord.value = words[questionIndex]
        setAnswers()
    }

    private fun setAnswers() {
        val correct = currentWord.value?.translations?.first { it.lang == answerLanguage }
        correct?.let { corr ->
            val mutList: MutableList<String> = mutableListOf()
            mutList.add(corr.text)
            while (mutList.size < 4) {
                val newAnswer = corr.text.replaceFirst("aeiouyäö".random(), "aeiouy".random(), true)
                if (!mutList.contains(newAnswer))
                    mutList.add(newAnswer)
            }
            _answers.value = mutList.shuffled()
        }
    }

    fun nextQuestion(): Boolean {
        questionIndex++
        return questionIndex < words.size
    }

    fun checkAnswer(answerIndex: Int): Boolean {
        if(checkDistance(answers.value?.get(answerIndex), answerLanguage) == 0){
            currentWord.value?.let {
                userCorrectAnswers.add(it)
            }
            return true
        }
        return false
    }

    private fun checkDistance(answer: String?, language: String): Int? {
        return currentWord.value?.translations
            ?.first { it.lang == language }
            ?.editDistance(answer)
    }
}