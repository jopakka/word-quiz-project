/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.joonasniemi.wordquizproject.database.user.UserDatabaseDao
import com.joonasniemi.wordquizproject.database.words.RoomWord
import com.joonasniemi.wordquizproject.database.words.WordDatabaseDao
import com.joonasniemi.wordquizproject.game.Quiz
import kotlinx.coroutines.launch

class GameViewModel(private val wordDatabase: WordDatabaseDao,
                    private val userDatabase: UserDatabaseDao) : ViewModel() {
    companion object {
        private const val TAG = "GameViewModel"
    }

    /**
     * Call Quiz setQuestion function and updates users total guesses.
     * Checks if current word can be found in wordDatabase,
     * if found then updates its times guessed value,
     * if not found then insert that word to database
     */
    fun setQuestion() {
        Quiz.setQuestion()

        viewModelScope.launch {
            try{
                Quiz.currentWord.value?.let {
                    userDatabase.updateTotalGuesses()
                    if(get(it.id, it.lang, Quiz.answerLanguage) == null)
                        insert(RoomWord(it.id, it.text, it.detail, it.lang,
                            Quiz.answerLanguage, 1, 0))
                    else
                        updateTimesGuessed(it.id, it.lang, Quiz.answerLanguage)
                }
            } catch (e: Exception){
                Log.e(TAG, e.message.toString())
            }
        }
    }

    /**
     * Updates current word right guesses amount.
     * After launch has completed calls [onCompleted] function
     */
    fun correctAnswer(onCompleted: () -> Unit) {
        viewModelScope.launch {
            try {
                Quiz.currentWord.value?.let {
                    updateRightGuessed(
                        it.id,
                        Quiz.language,
                        Quiz.answerLanguage
                    )
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
            }
        }.invokeOnCompletion {
            onCompleted()
        }
    }

    /**
     * Use wordDatabase insert function to insert new word
     */
    private suspend fun insert(word: RoomWord){
        wordDatabase.insert(word)
    }

    /**
     * Use wordDatabase updateTimesGuessed function to update times guesses
     * for selected word
     */
    private suspend fun updateTimesGuessed(key: Int, language: String, answerLanguage: String){
        wordDatabase.updateTimesGuessed(key, language, answerLanguage)
    }

    /**
     * Use wordDatabase updateRightGuessed function to update right guesses
     * for selected word
     */
    private suspend fun updateRightGuessed(key: Int, language: String, answerLanguage: String){
        wordDatabase.updateRightGuessed(key, language, answerLanguage)
        userDatabase.updateRightGuesses()
    }

    /**
     * Use wordDatabase get function get word from database
     */
    private suspend fun get(key: Int, language: String, answerLanguage: String) =
        wordDatabase.get(key, language, answerLanguage)

}

/**
 * ViewModelFactory for gameViewModel to pass arguments
 */
class GameViewModelFactory(private val wordDatabaseDao: WordDatabaseDao, private val userDatabaseDao: UserDatabaseDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(GameViewModel::class.java))
            return GameViewModel(wordDatabaseDao, userDatabaseDao) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

