package com.joonasniemi.wordquizproject

import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.SpannedString
import android.text.method.LinkMovementMethod
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.joonasniemi.wordquizproject.ui.mainmenu.LanguagesSpinnerAdapter
import com.joonasniemi.wordquizproject.network.Word
import java.util.*

@BindingAdapter("languages")
fun bindLanguages(spinner: Spinner, words: Set<Word>?) {
    val placeholderList = listOf(spinner.context.getString(R.string.empty_list_placeholder_text))
    val languages:List<String> = (words?.map { it.lang }?.toSet()?.toList() ?: placeholderList) as List<String>

    spinner.isEnabled = languages != placeholderList
    spinner.adapter = LanguagesSpinnerAdapter(spinner.context, languages)
}

@BindingAdapter("wikiLink")
fun bindWikipedia(textView: TextView, link: String?){
    link?.let {
        textView.text = HtmlCompat.fromHtml(textView.context.getString(R.string.wikipedia_link, link), HtmlCompat.FROM_HTML_MODE_COMPACT)
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.visibility = View.VISIBLE
    }
}

@BindingAdapter("wordText")
fun bindWordText(textView: TextView, word: Word){
    textView.text = word.text.capitalize(Locale.ROOT)
    if(word.detail != null)
         textView.append(" (${word.detail})")
}

@BindingAdapter("answerText")
fun bindAnswer(radioButton: RadioButton, answer: String){
    radioButton.text = answer.capitalize(Locale.ROOT)
}