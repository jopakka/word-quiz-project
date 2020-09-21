package com.joonasniemi.wordquizproject.ui.mainmenu

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.network.WordsRepository
import kotlinx.coroutines.launch

enum class WordsApiStatus { LOADING, ERROR, DONE }

class MainMenuViewModel(private val context: Context?): ViewModel() {
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

    /**
     * Gets words from [repository], then sets them to [_words] variable
     */
    private fun getWords(){
        viewModelScope.launch {
            _status.value = WordsApiStatus.LOADING
            try {
                _words.value = repository.getWords()
                _status.value = WordsApiStatus.DONE
                Log.i(TAG, "Words retrieved successfully")
            } catch (e: Exception){
                Log.e(TAG, e.toString())
                Toast.makeText(context, "Error while trying to fetch words", Toast.LENGTH_LONG).show()
                _status.value = WordsApiStatus.ERROR
            }
        }
    }
}

/**
 * ViewModelFactory for passing arguments from fragment
 */
class MainMenuViewModelFactory(private val context: Context?) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Context::class.java).newInstance(context)
    }
}