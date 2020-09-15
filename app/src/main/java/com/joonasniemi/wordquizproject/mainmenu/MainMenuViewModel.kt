package com.joonasniemi.wordquizproject.mainmenu

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.network.WordsApi
import kotlinx.coroutines.launch

enum class WordsApiStatus { LOADING, ERROR, DONE }

class MainMenuViewModel: ViewModel() {
    companion object {
        const val TAG = "MainMenuViewModel"
    }

    private val _status = MutableLiveData<WordsApiStatus>()
    val status: LiveData<WordsApiStatus>
        get() = _status

    private val _words = MutableLiveData<Set<Map<String, Set<Word>>>>()
    val words: LiveData<Set<Map<String, Set<Word>>>>
        get() = _words

    init {
        getWords()
    }

    private fun getWords(){
        viewModelScope.launch {
            _status.value = WordsApiStatus.LOADING
            try {
                _words.value = WordsApi.retrofitService.getWords()
                _status.value = WordsApiStatus.DONE
                Log.i(TAG, "Words retrieved successfully")
            } catch (e: Exception){
                Log.e(TAG, e.toString())
                _status.value = WordsApiStatus.ERROR
            }
        }
    }
}