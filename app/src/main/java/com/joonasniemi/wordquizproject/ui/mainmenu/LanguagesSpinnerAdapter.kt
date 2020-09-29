/*
 * Joonas Niemi
 * 1908997
 */

package com.joonasniemi.wordquizproject.ui.mainmenu

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.joonasniemi.wordquizproject.R
import java.util.*

/**
 * Custom [ArrayAdapter] for language spinners
 */
class LanguagesSpinnerAdapter(context: Context, private val languageList: List<String>,
                              private val resource: Int = R.layout.support_simple_spinner_dropdown_item
) : ArrayAdapter<String>(context, resource, languageList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return setView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return setView(position, convertView, parent)
    }

    /**
     * Disables first item on list
     */
    override fun isEnabled(position: Int): Boolean {
        return position != 0
    }

    /**
     * Sets first item text color to gray, and capitalize text
     */
    private fun setView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listItem = convertView ?: LayoutInflater.from(context)
            .inflate(resource, parent, false)

        if(position == 0)
            (listItem as TextView).setTextColor(Color.GRAY)

        val currentItem = languageList[position]
        (listItem as TextView).text = currentItem.capitalize(Locale.ROOT)
        return listItem
    }
}