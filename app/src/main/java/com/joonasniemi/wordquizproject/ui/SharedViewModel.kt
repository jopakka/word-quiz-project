/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.ui

import android.app.Application
import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.*
import com.google.android.material.snackbar.Snackbar
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.utils.Utils
import com.joonasniemi.wordquizproject.database.user.User
import com.joonasniemi.wordquizproject.database.user.UserDatabase
import com.joonasniemi.wordquizproject.game.Quiz
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.network.WordsNetworkRepository
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

enum class Status { LOADING, DONE, ERROR }

class SharedViewModel(application: Application) : ViewModel() {
    private val userDao = UserDatabase.getInstance(application).userDatabaseDao

    private val _allWords = MutableLiveData<Set<Word>>()
    val allWords: LiveData<Set<Word>>
        get() = _allWords

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    val user: LiveData<User> = userDao.getUser()

    init {
        _status.value = Status.LOADING
        if(Utils.isConnected(application)){
            viewModelScope.launch {
                _allWords.value = WordsNetworkRepository().getWords()
                _status.value = Status.DONE
            }
        } else {
            _status.value = Status.ERROR
        }
    }
}

class SharedViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SharedViewModel::class.java))
            return SharedViewModel(application) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}