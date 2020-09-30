/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.ui.mainmenu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.joonasniemi.wordquizproject.databinding.FragmentMainMenuBinding
import com.joonasniemi.wordquizproject.game.Quiz
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.ui.SharedViewModel
import com.joonasniemi.wordquizproject.ui.SharedViewModelFactory
import java.util.*

class MainMenuFragment : Fragment() {
    /**
     * Layouts bindings
     */
    private lateinit var binding: FragmentMainMenuBinding

    /**
     * Creates viewModel for fragment
     */
    private val mainMenuViewModel: MainMenuViewModel by viewModels()

    /**
     * Gets sharedViewModel from activityViewModels
     */
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

        /**
         * If user in [sharedViewModel] is null then play and stats buttons are not enabled
         * and reminderText is visible
         */
        sharedViewModel.user.observe(viewLifecycleOwner, {
            mainMenuViewModel.statusReady()
            if(it == null) {
                binding.playButton.isEnabled = false
                binding.statsButton.isEnabled = false
                binding.languageReminderText.visibility = View.VISIBLE
            } else {
                binding.playButton.isEnabled = true
                binding.statsButton.isEnabled = true
                binding.languageReminderText.visibility = View.GONE
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
            if (answerLanguage != null && list != null){
                Quiz.initGame(list, answerLanguage)
                it.findNavController()
                    .navigate(
                        MainMenuFragmentDirections.actionMainMenuFragmentToGameFragment())
            }
        }

        /**
         * Stats button onClickListener
         */
        binding.statsButton.setOnClickListener {
            findNavController()
                .navigate(MainMenuFragmentDirections.actionMainMenuFragmentToStatsFragment())
        }

        /**
         * Settings button onClickListener
         */
        binding.settingsButton.setOnClickListener {
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