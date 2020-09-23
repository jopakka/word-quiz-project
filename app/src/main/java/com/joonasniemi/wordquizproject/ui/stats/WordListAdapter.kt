package com.joonasniemi.wordquizproject.ui.stats

import android.app.Application
import android.content.Context
import android.graphics.Color
import android.util.Log
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
import com.joonasniemi.wordquizproject.database.WordDatabase
import com.joonasniemi.wordquizproject.databinding.WordRecyclerItemBinding
import com.joonasniemi.wordquizproject.network.Word
import kotlinx.android.synthetic.main.fragment_stats.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import java.util.*

class WordListAdapter :
    RecyclerView.Adapter<WordListAdapter.WordViewHolder>() {

    var data = setOf<Word>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        return WordViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        val item = data.elementAt(position)
        holder.bind(item)
    }

    override fun getItemCount() = data.size

    class WordViewHolder private constructor(itemView: View): RecyclerView.ViewHolder(itemView){
        companion object{
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
        private val checkText: TextView = itemView.findViewById(R.id.check_text)

        private val database = WordDatabase.getInstance(itemView.context).wordDatabaseDao
        private val language = "finnish" // TODO("Change to database query")

        fun bind(word: Word){
            title.text = word.text.capitalize(Locale.ROOT)
            bindWikipedia(wikipedia, word.wiki)
            bindImage(imageView, word.imgUrl)

            CoroutineScope(Main).launch {
                when(database.get(word.id, language)){
                    null -> {
                        checkMark.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_clear_24))
                        DrawableCompat.setTint(checkMark.drawable, ContextCompat.getColor(itemView.context, android.R.color.holo_red_dark))
                        checkText.text = itemView.context.getString(R.string.have_not_guessed)
                    }
                    else -> {
                        checkMark.setImageDrawable(ContextCompat.getDrawable(itemView.context, R.drawable.ic_baseline_check_24))
                        DrawableCompat.setTint(checkMark.drawable, ContextCompat.getColor(itemView.context, android.R.color.holo_green_light))
                        checkText.text = itemView.context.getString(R.string.have_guessed_right)
                    }
                }
            }
        }
    }
}