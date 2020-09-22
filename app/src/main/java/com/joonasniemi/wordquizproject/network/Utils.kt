package com.joonasniemi.wordquizproject.network

import android.os.Parcelable
import com.joonasniemi.wordquizproject.ui.game.GameType
import kotlinx.android.parcel.Parcelize

/**
 * Class for passing list of words to different fragment
 */
@Parcelize
data class GameArguments(val words: List<Word>, val answerLanguage: String, val gameType: GameType) : Parcelable

@Parcelize
data class AfterMatchArguments(val correctWords: List<Word>, val totalWords: Int): Parcelable