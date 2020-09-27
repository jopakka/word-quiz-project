package com.joonasniemi.wordquizproject.ui.mainmenu

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.databinding.FragmentMainMenuBinding
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.ui.SharedViewModel
import com.joonasniemi.wordquizproject.ui.SharedViewModelFactory
import com.joonasniemi.wordquizproject.utils.GameArguments
import java.util.*
import kotlin.properties.Delegates

class MainMenuFragment : Fragment() {
    private lateinit var binding: FragmentMainMenuBinding
    private val mainMenuViewModel: MainMenuViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMainMenuBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = mainMenuViewModel

        sharedViewModel.user.observe(viewLifecycleOwner, {
            mainMenuViewModel.statusReady()
            if(it == null) {
                // TODO("Inform user that languages needs to set")
                binding.playButton.isEnabled = false
                binding.statsButton.isEnabled = false
            } else {
                binding.playButton.isEnabled = true
                binding.statsButton.isEnabled = true
            }
        })

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
            val answerLanguage =
                sharedViewModel.user.value?.answerLanguage?.decapitalize(Locale.ROOT)
            val list = getShuffledList()
            if (answerLanguage != null && list != null)
                it.findNavController()
                    .navigate(
                        MainMenuFragmentDirections
                            .actionMainMenuFragmentToGameFragment(
                                GameArguments(list, answerLanguage)
                            )
                    )
        }

        binding.statsButton.setOnClickListener {
            findNavController()
                .navigate(MainMenuFragmentDirections.actionMainMenuFragmentToStatsFragment())
        }

        binding.setttingsButton.setOnClickListener {
            findNavController()
                .navigate(MainMenuFragmentDirections.actionMainMenuFragmentToSettingsFragment())
        }
    }

    /**
     * Returns List<Word> with maximum of [n] shuffled words in it
     */
    private fun getShuffledList(n: Int = 5): List<Word>? {
        return sharedViewModel.allWords.value?.filter { word ->
            word.lang == sharedViewModel.user.value?.language?.decapitalize(Locale.ROOT)
        }?.shuffled()?.take(n)
    }
}