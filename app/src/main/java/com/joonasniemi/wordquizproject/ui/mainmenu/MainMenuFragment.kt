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

        binding.currentLanguagesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.playButton.isEnabled = checkSpinnersNotEqual() && checkSpinnersNotZero()
                if(!checkSpinnersNotEqual())
                    binding.learningLanguagesSpinner.setSelection(0)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        binding.learningLanguagesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                binding.playButton.isEnabled = checkSpinnersNotEqual() && checkSpinnersNotZero()
                if(!checkSpinnersNotEqual())
                    binding.currentLanguagesSpinner.setSelection(0)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }

    private fun checkSpinnersNotEqual(): Boolean{
        return binding.currentLanguagesSpinner.selectedItem != binding.learningLanguagesSpinner.selectedItem
    }

    private fun checkSpinnersNotZero(): Boolean {
        return binding.currentLanguagesSpinner.selectedItemId != 0L
                && binding.learningLanguagesSpinner.selectedItemId != 0L
    }

    private fun getShuffledList(n: Int = 5) = WordList(
        viewModel.words.value?.filter { word ->
            word.lang == binding.currentLanguagesSpinner
                .selectedItem.toString().decapitalize(Locale.ROOT)
        }?.shuffled()?.take(n) ?: emptyList(),
        binding.learningLanguagesSpinner.selectedItem.toString().decapitalize(Locale.ROOT)
    )
}