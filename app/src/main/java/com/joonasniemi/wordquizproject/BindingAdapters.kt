/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject

import android.text.method.LinkMovementMethod
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.ui.mainmenu.LanguagesSpinnerAdapter
import java.util.*

@BindingAdapter("languages")
fun bindLanguages(spinner: Spinner, words: Set<Word>?) {
    val list = mutableListOf(spinner.context.getString(R.string.select_language))
    list.addAll(words?.map { w -> w.lang }?.sorted()?.distinct() ?: emptyList())

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
fun bindWordText(textView: TextView, word: Word?){
    textView.text = word?.text?.capitalize(Locale.ROOT)
    word?.detail?.let { textView.append(" ($it)") }
}

@BindingAdapter("answerText")
fun bindAnswer(radioButton: RadioButton, answer: String?){
    radioButton.text = answer?.capitalize(Locale.ROOT)
}

@BindingAdapter("imageUrl")
fun bindImage(imageView: ImageView, imgUrl: String?){
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imageView.context)
            .load(imgUri)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_icon_with_animation)
                .error(R.drawable.questionmark))
            .into(imageView)
    } ?: imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.questionmark))
}

@BindingAdapter("afterMatchTitle",)
fun bindAfterMatchTitle(textView: TextView, rating: Int){
    when(rating) {
        0 -> textView.text = textView.context.getString(R.string.correct_none)
        1 -> textView.text = textView.context.getString(R.string.correct_under_half)
        2 -> textView.text = textView.context.getString(R.string.correct_over_half)
        3 -> textView.text = textView.context.getString(R.string.correct_all)
    }
}

@BindingAdapter("afterMatchText", "maxWords", requireAll = true)
fun bindAfterMatchText(textView: TextView, corrects: Int, max: Int){
    when (corrects) {
        0 -> {
            textView.text = textView.context.getString(R.string.correct_text_none)
        }
        max -> {
            textView.text = textView.context.getString(R.string.correct_text_all)
        }
        else -> {
            textView.text = textView.context.getString(R.string.correct_text_some, corrects, max)
        }
    }
}

@BindingAdapter("afterMatchRating")
fun bindAfterMatchRating(imageView: ImageView, rating: Int) {
    when(rating){
        0 -> imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.smiley_bad))
        1 -> imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.smiley_ok))
        2 -> imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.smiley_good))
        3 -> imageView.setImageDrawable(ContextCompat.getDrawable(imageView.context, R.drawable.smiley_excelent))
    }
}

@BindingAdapter("statTexts")
fun bindStatTexts(textView: TextView, value: Int){
    when(textView.id){
        R.id.total_guesses_text ->
            textView.text = textView.context.getString(R.string.total_guesses, value)
        R.id.total_right_guesses_text ->
            textView.text = textView.context.getString(R.string.total_right_guesses, value)
        R.id.total_words_text ->
            textView.text = textView.context.getString(R.string.total_words, value)
    }
}