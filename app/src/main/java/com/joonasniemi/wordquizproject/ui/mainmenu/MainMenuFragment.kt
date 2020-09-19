package com.joonasniemi.wordquizproject.ui.mainmenu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.Bindable
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

    /**
     * Creates viewModel for fragment
     */
    private val viewModel: MainMenuViewModel by lazy {
        ViewModelProvider(
            this,
            MainMenuViewModelFactory(context)
        ).get(MainMenuViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainMenuBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setListeners()

        return binding.root
    }

    /**
     * Initialize listeners to different elements
     */
    private fun setListeners() {
        /**
         * Play button onClickListener
         */
        binding.playButton.setOnClickListener {
            it.findNavController()
                .navigate(
                    MainMenuFragmentDirections
                        .actionMainMenuFragmentToGameFragment(getShuffledList())
                )
        }

        /**
         * Current language spinner onItemSelectedListener
         */
        binding.currentLanguagesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.playButton.isEnabled = checkSpinnersNotEqual() && checkSpinnersNotZero()
                    if (!checkSpinnersNotEqual())
                        binding.learningLanguagesSpinner.setSelection(0)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    parent?.setSelection(0)
                }
            }

        /**
         * Learning language spinner onItemSelectedSpinner
         */
        binding.learningLanguagesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    binding.playButton.isEnabled = checkSpinnersNotEqual() && checkSpinnersNotZero()
                    if (!checkSpinnersNotEqual())
                        binding.currentLanguagesSpinner.setSelection(0)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    parent?.setSelection(0)
                }
            }
    }

    /**
     * Checks that are two language spinners equals
     */
    private fun checkSpinnersNotEqual(): Boolean {
        return binding.currentLanguagesSpinner.selectedItem != binding.learningLanguagesSpinner.selectedItem
    }

    /**
     * Checks that two language spinners values aren't 0
     */
    private fun checkSpinnersNotZero(): Boolean {
        return binding.currentLanguagesSpinner.selectedItemId != 0L
                && binding.learningLanguagesSpinner.selectedItemId != 0L
    }

    /**
     * Returns [WordList] with maximum of [n] shuffled words in it
     */
    private fun getShuffledList(n: Int = 5): WordList {
        val list = viewModel.words.value?.filter { word ->
            word.lang == binding.currentLanguagesSpinner.selectedItem.toString()
                .decapitalize(Locale.ROOT)
        }?.shuffled()?.take(n) ?: emptyList()
        return WordList(
            list, binding.learningLanguagesSpinner.selectedItem.toString().decapitalize(Locale.ROOT)
        )
    }
}