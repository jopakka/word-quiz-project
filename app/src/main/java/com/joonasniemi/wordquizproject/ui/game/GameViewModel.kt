package com.joonasniemi.wordquizproject.ui.game

import android.util.Log
import androidx.lifecycle.*
import com.joonasniemi.wordquizproject.database.words.RoomWord
import com.joonasniemi.wordquizproject.database.words.WordDatabaseDao
import com.joonasniemi.wordquizproject.network.Word
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class GameViewModel(dataSource: WordDatabaseDao) : ViewModel() {
    companion object {
        private const val TAG = "GameViewModel"
    }

    val database = dataSource

    private val words: MutableList<Word> = mutableListOf()
    lateinit var answerLanguage: String

    private val _answers = MutableLiveData<List<String>>()
    val answers: LiveData<List<String>>
        get() = _answers

    private val _currentWord = MutableLiveData<Word>()
    val currentWord: LiveData<Word>
        get() = _currentWord

    var questionIndex = 0
    val userCorrectAnswers = mutableListOf<Word>()

    fun initGame(list: List<Word>, answerLanguage: String) {
        words.addAll(list)
        this.answerLanguage = answerLanguage
    }

    fun setQuestion() {
        _currentWord.value = words[questionIndex]
        setAnswers()

        viewModelScope.launch {
            try{
                currentWord.value?.let {
                    if(get(it.id, answerLanguage) == null)
                        insert(RoomWord(it.id, it.text, it.detail, answerLanguage, 1, 0))
                    else
                        updateTimesGuessed(it.id, answerLanguage)
                }
            } catch (e: Exception){
                Log.e(TAG, e.message.toString())
            }
        }
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

    private suspend fun insert(word: RoomWord){
        database.insert(word)
    }

    private suspend fun updateTimesGuessed(key: Int, fromLang: String){
        database.updateTimesGuessed(key, fromLang)
    }

    suspend fun updateRightGuessed(key: Int, fromLang: String){
        database.updateRightGuessed(key, fromLang)
    }

    private suspend fun get(key: Int, fromLang: String) = database.get(key, fromLang)
}

class GameViewModelFactory(private val dataSource: WordDatabaseDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameViewModel::class.java))
            return GameViewModel(dataSource) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

