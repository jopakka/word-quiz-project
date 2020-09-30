/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.ui.stats

import android.content.Context
import androidx.lifecycle.*
import com.joonasniemi.wordquizproject.database.words.RoomWord
import com.joonasniemi.wordquizproject.database.words.WordDatabase
import com.joonasniemi.wordquizproject.ui.Status
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class StatsViewModel(context: Context): ViewModel() {
    var totalGuesses = MutableLiveData<Int>()
    var rightGuesses = MutableLiveData<Int>()
    val wordCount = MutableLiveData<Int>()

    private val wordDatabase = WordDatabase.getInstance(context).wordDatabaseDao
    lateinit var guessedWords: List<RoomWord>

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    init {
        totalGuesses.value = 0
        rightGuesses.value = 0
        _status.value = Status.LOADING
    }

    /**
     * Counts how many time user has guessed every word from [language] to [answerLanguage]
     */
    fun initGuessedWords(language: String, answerLanguage: String){
        viewModelScope.launch {
            try {
                guessedWords = wordDatabase.getAll(language, answerLanguage)
                totalGuesses.value = guessedWords.map { it.timesGuessed }.sum()
                rightGuesses.value = guessedWords.map { it.rightGuesses }.sum()
                _status.value = Status.DONE
            } catch (e: Exception) {
                _status.value = Status.ERROR
            }
        }
    }
}

/**
 * ViewModelFactory for statsViewModel to pass arguments
 */
class StatsViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StatsViewModel::class.java))
            return StatsViewModel(context) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}