package com.joonasniemi.wordquizproject.ui.game

import android.util.Log
import androidx.lifecycle.*
import com.joonasniemi.wordquizproject.database.user.UserDatabaseDao
import com.joonasniemi.wordquizproject.database.words.RoomWord
import com.joonasniemi.wordquizproject.database.words.WordDatabaseDao
import com.joonasniemi.wordquizproject.network.Word
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class GameViewModel(private val wordDatabase: WordDatabaseDao, private val userDatabase: UserDatabaseDao) : ViewModel() {
    companion object {
        private const val TAG = "GameViewModel"
    }

    private val words: MutableList<Word> = mutableListOf()
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

    fun initGame(list: List<Word>, answerLanguage: String) {
        words.addAll(list)
        this.answerLanguage = answerLanguage
        language = list.first().lang
        if(currentWord.value != words[questionIndex])
            setQuestion()
    }

    fun setQuestion() {
        _currentWord.value = words[questionIndex]
        setAnswers()

        viewModelScope.launch {
            try{
                currentWord.value?.let {
                    userDatabase.updateTotalGuesses()
                    if(get(it.id, it.lang, answerLanguage) == null)
                        insert(RoomWord(it.id, it.text, it.detail, it.lang, answerLanguage, 1, 0))
                    else
                        updateTimesGuessed(it.id, it.lang, answerLanguage)
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
        wordDatabase.insert(word)
    }

    private suspend fun updateTimesGuessed(key: Int, language: String, answerLanguage: String){
        wordDatabase.updateTimesGuessed(key, language, answerLanguage)
    }

    suspend fun updateRightGuessed(key: Int, language: String, answerLanguage: String){
        wordDatabase.updateRightGuessed(key, language, answerLanguage)
        userDatabase.updateRightGuesses()
    }

    private suspend fun get(key: Int, language: String, answerLanguage: String) =
        wordDatabase.get(key, language, answerLanguage)
}

class GameViewModelFactory(private val wordDatabaseDao: WordDatabaseDao, private val userDatabaseDao: UserDatabaseDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameViewModel::class.java))
            return GameViewModel(wordDatabaseDao, userDatabaseDao) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

