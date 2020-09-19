package com.joonasniemi.wordquizproject.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * Class for passing list of words to different fragment
 */
@Parcelize
data class WordList(val list: List<Word>, val translationLanguage: String) : Parcelable