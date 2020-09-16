package com.joonasniemi.wordquizproject

import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import com.joonasniemi.wordquizproject.mainmenu.WordsApiStatus
import com.joonasniemi.wordquizproject.network.Word
import java.util.*
import java.util.stream.Collectors

@BindingAdapter("wordsApiStatus")
fun bindStatus(statusImageView: ImageView, status: WordsApiStatus) {
    when (status) {
        WordsApiStatus.LOADING -> {
            statusImageView.visibility = View.VISIBLE
            TODO("statusImageView.setImageResource()")
        }
        WordsApiStatus.ERROR -> {
            statusImageView.visibility = View.VISIBLE
            statusImageView.setImageResource(R.drawable.ic_connection_error)
        }
        WordsApiStatus.DONE -> {
            statusImageView.visibility = View.GONE
        }
    }
}

@BindingAdapter("languages")
fun bindLanguages(spinner: Spinner, values: Set<Word>?) {
    val items = values?.map { it.lang.capitalize(Locale.ROOT) }?.toSet()?.toList() ?: listOf(R.string.empty_list_placeholder_text)

    spinner.adapter = ArrayAdapter(
        spinner.context, R.layout.support_simple_spinner_dropdown_item,
        items
    )
}