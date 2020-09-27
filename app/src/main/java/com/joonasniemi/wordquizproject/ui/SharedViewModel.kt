package com.joonasniemi.wordquizproject.ui

import android.app.Application
import androidx.lifecycle.*
import com.joonasniemi.wordquizproject.database.user.User
import com.joonasniemi.wordquizproject.database.user.UserDatabase
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.network.WordsNetworkRepository
import kotlinx.coroutines.launch

enum class Status { LOADING, DONE, ERROR }

class SharedViewModel(application: Application) : ViewModel() {
    private val userDao = UserDatabase.getInstance(application).userDatabaseDao

    private val _allWords = MutableLiveData<Set<Word>>()
    val allWords: LiveData<Set<Word>>
        get() = _allWords

    val user: LiveData<User> = userDao.getUser()

    init {
        viewModelScope.launch {
            _allWords.value = WordsNetworkRepository().getWords()
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