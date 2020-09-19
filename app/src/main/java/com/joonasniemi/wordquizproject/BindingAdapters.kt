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
import androidx.databinding.Bindable
import androidx.databinding.BindingAdapter
import com.joonasniemi.wordquizproject.databinding.FragmentMainMenuBinding
import com.joonasniemi.wordquizproject.ui.mainmenu.LanguagesSpinnerAdapter
import com.joonasniemi.wordquizproject.network.Word
import java.util.*

@BindingAdapter("languages")
fun bindLanguages(spinner: Spinner, words: Set<Word>?) {
    val list = mutableListOf(spinner.context.getString(R.string.select_language))
    list.addAll(words?.map { w -> w.lang }?.sorted() ?: emptyList())
    list.addAll(listOf("Finnish", "English", "Swedish").sorted())

    spinner.isEnabled = list.size > 1
    spinner.adapter = LanguagesSpinnerAdapter(spinner.context, list)
}

@BindingAdapter("wikiLink")
fun bindWikipedia(textView: TextView, link: String?){
    when(link){
        null -> textView.visibility = View.INVISIBLE
        else -> {
            textView.text = HtmlCompat.fromHtml(textView.context.getString(R.string.wikipedia_link, link), HtmlCompat.FROM_HTML_MODE_COMPACT)
            textView.movementMethod = LinkMovementMethod.getInstance()
            textView.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("wordText")
fun bindWordText(textView: TextView, word: Word){
    textView.text = word.text.capitalize(Locale.ROOT)
    word.detail?.let { textView.append(" ($it)") }
}

@BindingAdapter("answerText")
fun bindAnswer(radioButton: RadioButton, answer: String){
    radioButton.text = answer.capitalize(Locale.ROOT)
}