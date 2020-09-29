/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.ui.aftermatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joonasniemi.wordquizproject.utils.AfterMatchArguments

class AfterMatchViewModel(args: AfterMatchArguments): ViewModel() {
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