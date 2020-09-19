package com.joonasniemi.wordquizproject.ui.game

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.network.WordList

enum class AnswersStatus { LOADING, DONE, ERROR }

class GameViewModel(val args: Bundle?) : ViewModel() {
    companion object {
        const val TAG = "GameViewModel"
    }

    private val _answersStatus = MutableLiveData<AnswersStatus>()
    val answersStatus: LiveData<AnswersStatus>
        get() = _answersStatus

    private var words: List<Word> = emptyList()

    private val _answers = mutableSetOf<String>()
    val answers: List<String>
        get() = _answers.toList()

    private val _currentWord = MutableLiveData<Word>()
    val currentWord: LiveData<Word>
        get() = _currentWord

    val learningLanguage: String

    init {
        _answersStatus.value = AnswersStatus.LOADING
        words = (args?.get("wordList") as WordList).list
        learningLanguage = (args.get("wordList") as WordList).translationLanguage
        _currentWord.value = words[0]
        setAnswers()
    }

    private fun setAnswers(){
        val correct = currentWord.value?.translations?.first { it.lang == learningLanguage }
        correct?.text?.let { _answers.add(it) }
        while (_answers.size < 4){
            correct?.text?.let {
                _answers.add(it.replaceFirst("aeiouyäö".random(), "aeiouy".random(), true))
            }
        }
    }

}

class GameViewModelFactory(private val args: Bundle?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        Log.i("GameViewModelFactory", (args?.get("wordList") as WordList).list[0].translations.toString())
        return modelClass.getConstructor(Bundle::class.java).newInstance(args)
    }
}