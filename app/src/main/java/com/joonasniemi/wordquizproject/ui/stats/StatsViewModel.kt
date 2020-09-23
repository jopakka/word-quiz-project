package com.joonasniemi.wordquizproject.ui.stats

import androidx.lifecycle.*
import com.joonasniemi.wordquizproject.database.WordDatabaseDao
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.network.WordsRepository
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class StatsViewModel(dataSource: WordDatabaseDao): ViewModel() {

    private val _wordSet = MutableLiveData<Set<Word>>()
    val wordSet: LiveData<Set<Word>>
        get() = _wordSet

    init {
        viewModelScope.launch {
            _wordSet.value = WordsRepository().getWords()
        }
    }
}

class StatsViewModelFactory(private val dataSource: WordDatabaseDao) : ViewModelProvider.Factory {

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(StatsViewModel::class.java))
            return StatsViewModel(dataSource) as T
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}