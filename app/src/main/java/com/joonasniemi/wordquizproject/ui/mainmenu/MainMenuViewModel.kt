package com.joonasniemi.wordquizproject.ui.mainmenu

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joonasniemi.wordquizproject.ui.Status

class MainMenuViewModel : ViewModel() {
    companion object {
        private const val TAG = "MainMenuViewModel"
    }

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    init {
        statusLoading()
    }

    fun statusReady() {
        _status.value = Status.DONE
    }

    fun statusLoading() {
        _status.value = Status.LOADING
    }
}