/*
 * Joonas Niemi
 * 1908997
 */

package com.joonasniemi.wordquizproject.ui.game

import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.joonasniemi.wordquizproject.database.user.UserDatabaseDao
import com.joonasniemi.wordquizproject.database.words.RoomWord
import com.joonasniemi.wordquizproject.database.words.WordDatabaseDao
import com.joonasniemi.wordquizproject.game.Quiz
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.utils.AfterMatchArguments
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class GameViewModel(private val wordDatabase: WordDatabaseDao, private val userDatabase: UserDatabaseDao) : ViewModel() {
    companion object {
        private const val TAG = "GameViewModel"
    }

    val quiz: Quiz = Quiz.instance

    fun setQuestion() {
        quiz.setQuestion()

        viewModelScope.launch {
            try{
                quiz.currentWord.value?.let {
                    userDatabase.updateTotalGuesses()
                    if(get(it.id, it.lang, quiz.answerLanguage) == null)
                        insert(RoomWord(it.id, it.text, it.detail, it.lang,
                            quiz.answerLanguage, 1, 0))
                    else
                        updateTimesGuessed(it.id, it.lang, quiz.answerLanguage)
                }
            } catch (e: Exception){
                Log.e(TAG, e.message.toString())
            }
        }
    }

    fun correctAnswer(onCompleted: () -> Unit) {
        viewModelScope.launch {
            try {
                quiz.currentWord.value?.let {
                    updateRightGuessed(
                        it.id,
                        quiz.language,
                        quiz.answerLanguage
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

