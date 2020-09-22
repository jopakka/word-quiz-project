package com.joonasniemi.wordquizproject.ui.aftermatch

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joonasniemi.wordquizproject.network.AfterMatchArguments

class AfterMatchViewModel(val args: AfterMatchArguments): ViewModel() {
    companion object {
        private const val TAG = "AfterMatchViewModel"
    }

    val corrects = args.correctWords.size
    val max = args.totalWords

}

/**
 * ViewModelFactory for passing arguments from fragment
 */
class AfterMatchViewModelFactory(private val args: AfterMatchArguments) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(AfterMatchArguments::class.java).newInstance(args)
    }
}