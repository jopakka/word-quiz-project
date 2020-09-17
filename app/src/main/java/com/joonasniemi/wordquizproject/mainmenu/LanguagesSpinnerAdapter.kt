package com.joonasniemi.wordquizproject.mainmenu

import android.content.Context
import android.database.DataSetObserver
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.TextView
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.network.Word
import java.util.*

class LanguagesSpinnerAdapter(context: Context, private val languageList: List<String>,
                              resource: Int = R.layout.support_simple_spinner_dropdown_item
) : ArrayAdapter<String>(context, resource, languageList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listItem = convertView ?: LayoutInflater.from(context).inflate(
                R.layout.support_simple_spinner_dropdown_item, parent, false)

        val currentItem = languageList[position]
        (listItem as TextView).text = currentItem.capitalize(Locale.ROOT)
        return listItem
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listItem = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.support_simple_spinner_dropdown_item, parent, false)

        val currentItem = languageList[position]
        (listItem as TextView).text = currentItem.capitalize(Locale.ROOT)
        return listItem
    }


}