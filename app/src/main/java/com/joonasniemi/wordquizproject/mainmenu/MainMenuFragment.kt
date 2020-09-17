package com.joonasniemi.wordquizproject.mainmenu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.databinding.FragmentMainMenuBinding
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.network.WordList
import org.w3c.dom.Text
import java.util.*

class MainMenuFragment : Fragment() {
    companion object {
        private const val TAG = "MainMenuFragment"
    }

    private lateinit var binding: FragmentMainMenuBinding

    private val viewModel: MainMenuViewModel by lazy {
        ViewModelProvider(this).get(MainMenuViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainMenuBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setOnClickListeners()

        return binding.root
    }

    private fun setOnClickListeners() {
        binding.playButton.setOnClickListener {
            val list = WordList(viewModel.words.value?.filter { word ->
                word.lang == binding.currentLanguagesSpinner
                    .selectedItem.toString().decapitalize(Locale.ROOT) }?.shuffled()?.take(5)?.toSet() ?: emptySet())

            it.findNavController()
                .navigate(MainMenuFragmentDirections.actionMainMenuFragmentToGameFragment(list))
        }
    }
}