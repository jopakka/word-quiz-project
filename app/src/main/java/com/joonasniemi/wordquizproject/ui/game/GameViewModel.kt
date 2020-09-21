package com.joonasniemi.wordquizproject.ui.game

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joonasniemi.wordquizproject.databinding.FragmentGameBinding
import com.joonasniemi.wordquizproject.network.Word

enum class GameType { MULTI, TEXT }

class GameViewModel : ViewModel() {
    companion object {
        const val TAG = "GameViewModel"
    }

    private val words: MutableList<Word> = mutableListOf()

    private lateinit var answerLanguage: String

    private val _answers = MutableLiveData<List<String>>()
    val answers: LiveData<List<String>>
        get() = _answers

    private val _currentWord = MutableLiveData<Word>()
    val currentWord: LiveData<Word>
        get() = _currentWord

    var questionIndex = 0

    lateinit var gameType: GameType

    fun initGame(list: List<Word>, answerLanguage: String, gameType: GameType) {
        words.addAll(list)
        this.gameType = gameType
        this.answerLanguage = answerLanguage
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
}

