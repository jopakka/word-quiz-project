/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.ui.aftermatch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.joonasniemi.wordquizproject.game.Quiz

class AfterMatchViewModel : ViewModel() {
    val corrects: Int = Quiz.userCorrectAnswers.size
    val max: Int = Quiz.maxQuestions

    private val _rating = MutableLiveData<Int>()
    val rating: LiveData<Int>
        get() = _rating

    init {
        _rating.value = rating()
        Quiz.resetGame()
    }

    /**
     * Return rating value depending how good user did
     */
    private fun rating(): Int {
        return when {
            corrects == 0 -> 0
            corrects > 0 && corrects <= max / 2 -> 1
            corrects > max / 2 && corrects < max -> 2
            else -> 3
        }
    }
}