package com.joonasniemi.wordquizproject.ui.stats

import androidx.lifecycle.*

class StatsViewModel: ViewModel() {
    var totalGuesses = MutableLiveData<Int>()
    var rightGuesses = MutableLiveData<Int>()
    val wordCount = MutableLiveData<Int>()

    init {
        totalGuesses.value = 0
        rightGuesses.value = 0
    }
}