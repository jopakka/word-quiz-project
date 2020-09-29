/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.ui.aftermatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.joonasniemi.wordquizproject.game.Quiz
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.utils.AfterMatchArguments

class AfterMatchViewModel : ViewModel() {
    val corrects: Int = Quiz.userCorrectAnswers.size
    val max: Int = Quiz.maxQuestions

    init {
        Quiz.resetGame()
    }
}