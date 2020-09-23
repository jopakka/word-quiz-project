package com.joonasniemi.wordquizproject.ui.stats

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.network.WordsRepository
import kotlinx.coroutines.launch

class StatsViewModel: ViewModel() {

    private val _wordSet = MutableLiveData<Set<Word>>()
    val wordSet: LiveData<Set<Word>>
        get() = _wordSet

    init {
        viewModelScope.launch {
            _wordSet.value = WordsRepository().getWords()
        }
    }
}