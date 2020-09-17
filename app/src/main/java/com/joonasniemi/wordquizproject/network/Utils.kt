package com.joonasniemi.wordquizproject.network

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class WordList(val list: Set<Word>) : Parcelable