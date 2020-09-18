package com.joonasniemi.wordquizproject.ui.mainmenu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.joonasniemi.wordquizproject.databinding.FragmentMainMenuBinding
import com.joonasniemi.wordquizproject.network.WordList
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

        setListeners()

        return binding.root
    }

    private fun setListeners() {
        binding.playButton.setOnClickListener {
            it.findNavController()
                .navigate(
                    MainMenuFragmentDirections
                        .actionMainMenuFragmentToGameFragment(getShuffledList())
                )
        }
    }

    private fun getShuffledList(n: Int = 5) = WordList(
        viewModel.words.value?.filter { word ->
            word.lang == binding.currentLanguagesSpinner
                .selectedItem.toString().decapitalize(Locale.ROOT)
        }?.shuffled()?.take(n) ?: emptyList(),
        binding.learningLanguagesSpinner.selectedItem.toString().decapitalize(Locale.ROOT)
    )
}