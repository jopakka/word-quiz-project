/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.ui.stats

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.bindImage
import com.joonasniemi.wordquizproject.bindWikipedia
import com.joonasniemi.wordquizproject.database.words.RoomWord
import com.joonasniemi.wordquizproject.network.Word
import java.util.*

/**
 * Adapter for stats menu recyclerView
 */
class WordListAdapter :
    RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

    var wordSet = setOf<Word>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var guessedWords = listOf<RoomWord>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val item = wordSet.elementAt(position)
        holder.bind(item, guessedWords)
    }

    override fun getItemCount() = wordSet.size

    class WordViewHolder private constructor(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        companion object {
            fun from(parent: ViewGroup): WordViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.word_recycler_item, parent, false)
                return WordViewHolder(view)
            }
        }

        private val imageView: ImageView = itemView.findViewById(R.id.image)
        private val title: TextView = itemView.findViewById(R.id.title)
        private val wikipedia: TextView = itemView.findViewById(R.id.wikipedia_text)
        private val checkMark: ImageView = itemView.findViewById(R.id.check_image)
        private val guessesText: TextView = itemView.findViewById(R.id.guesses_text)
        private val rightGuessesText: TextView = itemView.findViewById(R.id.right_guesses_text)

        fun bind(word: Word, guessedWords: List<RoomWord>) {
            title.text = word.text.capitalize(Locale.ROOT)
            bindWikipedia(wikipedia, word.wiki)
            bindImage(imageView, word.imgUrl)

            /**
             * Sets texts for single item in recyclerView
             *
             * If user has guessed word right then word has checkMark and green color
             * else it has X and red color
             */
            when(val roomWord = guessedWords.firstOrNull { word.isTranslation(it.text) }){
                null -> {
                    guessesText.text = itemView.context.getString(R.string.guesses, 0)
                    rightGuessesText.text = itemView.context.getString(R.string.right_guesses, 0)
                }
                else -> {
                    guessesText.text = itemView.context.getString(R.string.guesses, roomWord.timesGuessed)
                    rightGuessesText.text = itemView.context.getString(R.string.right_guesses, roomWord.rightGuesses)
                    if(roomWord.rightGuesses > 0){
                        checkMark.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_check_24))
                        DrawableCompat.setTint(checkMark.drawable, ContextCompat.getColor(itemView.context, android.R.color.holo_green_light))
                    } else {
                        checkMark.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_clear_24))
                        DrawableCompat.setTint(checkMark.drawable, ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark))
                    }
                }
            }
        }
    }
}