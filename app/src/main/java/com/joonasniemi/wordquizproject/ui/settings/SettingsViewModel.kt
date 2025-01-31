/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.ui.settings

import android.app.Application
import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import androidx.lifecycle.*
import com.joonasniemi.wordquizproject.database.user.User
import com.joonasniemi.wordquizproject.database.user.UserDatabase
import com.joonasniemi.wordquizproject.database.user.UserDatabaseDao
import com.joonasniemi.wordquizproject.ui.Status
import kotlinx.coroutines.launch
import kotlin.Exception

class SettingsViewModel(private val userDatabase: UserDatabaseDao) : ViewModel() {
    companion object {
        private const val TAG = "SettingsViewModel"
    }

    /**
     * Holds selected language spinner value
     */
    val selectedLanguage = MutableLiveData<String>()

    /**
     * Holds selected answer language spinner value
     */
    val selectedAnswerLanguage = MutableLiveData<String>()

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    private fun statusReady() {
        _status.value = Status.DONE
    }

    private fun statusLoading() {
        _status.value = Status.LOADING
    }

    private fun statusError() {
        _status.value = Status.ERROR
    }

    /**
     * Tries to insert user to database if not exist.
     * Else updates users languages
     */
    fun insert(user: User) {
        statusLoading()
        viewModelScope.launch {
            try {
                userDatabase.insert(user)
                statusReady()
            } catch (sqlError: SQLiteConstraintException) {
                Log.w(TAG, sqlError.message.toString())
                Log.i(TAG, "Trying to update languages")
                try {
                    userDatabase.updateLanguages(user.language, user.answerLanguage)
                    statusReady()
                } catch (e: Exception) {
                    Log.e(TAG, e.message.toString())
                    statusError()
                }
            } catch (e: Exception) {
                Log.e(TAG, e.message.toString())
                statusError()
            }
        }
    }
}

/**
 * ViewModelFactory for settingsViewModel to pass arguments
 */
class SettingsViewModelFactory(private val userDatabase: UserDatabaseDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingsViewModel::class.java))
            return SettingsViewModel(userDatabase) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}