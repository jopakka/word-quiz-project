/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.utils

import android.os.Parcelable
import com.joonasniemi.wordquizproject.network.Word
import kotlinx.android.parcel.Parcelize

/**
 * Class for passing list of words to different fragment
 */
@Parcelize
data class GameArguments(val words: List<Word>, val answerLanguage: String) : Parcelable

@Parcelize
data class AfterMatchArguments(val correctWords: List<Word>, val totalWords: Int): Parcelable