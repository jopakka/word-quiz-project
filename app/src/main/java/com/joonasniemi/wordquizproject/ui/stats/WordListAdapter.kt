package com.joonasniemi.wordquizproject.ui.stats

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.bindImage
import com.joonasniemi.wordquizproject.bindWikipedia
import com.joonasniemi.wordquizproject.network.Word
import kotlinx.android.synthetic.main.fragment_stats.view.*
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

        fun bind(word: Word){
            val res = itemView.context.resources
            title.text = word.text.capitalize(Locale.ROOT)
            bindWikipedia(wikipedia, word.wiki)
            bindImage(imageView, word.imgUrl)

            // TODO("Check if word is found in Room database")
            // Then display correct text and icon
        }
    }
}