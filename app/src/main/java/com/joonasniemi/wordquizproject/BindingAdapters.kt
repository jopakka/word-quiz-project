package com.joonasniemi.wordquizproject

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.joonasniemi.wordquizproject.mainmenu.LanguagesSpinnerAdapter
import com.joonasniemi.wordquizproject.network.Word
import java.util.*

@BindingAdapter("languages")
fun bindLanguages(spinner: Spinner, words: Set<Word>?) {
    val placeholderList = listOf(spinner.context.getString(R.string.empty_list_placeholder_text))
    val languages:List<String> = words?.map { it.lang }?.toSet()?.toList() ?: placeholderList

    spinner.isEnabled = languages != placeholderList
    spinner.adapter = LanguagesSpinnerAdapter(spinner.context, languages)
}