package com.joonasniemi.wordquizproject.ui.mainmenu

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.databinding.Bindable
import androidx.lifecycle.*
import com.joonasniemi.wordquizproject.databinding.FragmentMainMenuBinding
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.network.WordList
import com.joonasniemi.wordquizproject.network.WordsRepository
import kotlinx.coroutines.launch

enum class WordsApiStatus { LOADING, ERROR, DONE }

class MainMenuViewModel(): ViewModel() {
    companion object {
        private const val TAG = "MainMenuViewModel"
    }

    private val repository = WordsRepository()

    private val _status = MutableLiveData<WordsApiStatus>()
    val status: LiveData<WordsApiStatus>
        get() = _status

    private val _words = MutableLiveData<Set<Word>>()
    val words: LiveData<Set<Word>>
        get() = _words

    init {
        getWords()
    }

    private fun getWords(){
        viewModelScope.launch {
            _status.value = WordsApiStatus.LOADING
            try {
                _words.value = repository.getWords()
                _status.value = WordsApiStatus.DONE
                Log.i(TAG, "Words retrieved successfully")
            } catch (e: Exception){
                Log.e(TAG, e.toString())
                _status.value = WordsApiStatus.ERROR
            }
        }
    }
}