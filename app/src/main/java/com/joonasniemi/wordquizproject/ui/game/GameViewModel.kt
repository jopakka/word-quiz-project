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

class GameViewModel(private val wordDatabase: WordDatabaseDao, private val userDatabase: UserDatabaseDao) : ViewModel() {
    companion object {
        private const val TAG = "GameViewModel"
    }

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

    private suspend fun insert(word: RoomWord){
        wordDatabase.insert(word)
    }

    private suspend fun updateTimesGuessed(key: Int, language: String, answerLanguage: String){
        wordDatabase.updateTimesGuessed(key, language, answerLanguage)
    }

    private suspend fun updateRightGuessed(key: Int, language: String, answerLanguage: String){
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

