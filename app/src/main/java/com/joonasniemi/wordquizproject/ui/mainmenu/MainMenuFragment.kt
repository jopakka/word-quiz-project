package com.joonasniemi.wordquizproject.ui.mainmenu

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.database.user.UserDatabase
import com.joonasniemi.wordquizproject.database.words.WordDatabase
import com.joonasniemi.wordquizproject.databinding.FragmentMainMenuBinding
import com.joonasniemi.wordquizproject.utils.GameArguments
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.ui.SharedViewModel
import com.joonasniemi.wordquizproject.ui.SharedViewModelFactory
import kotlinx.coroutines.launch
import java.util.*

class MainMenuFragment : Fragment() {
    companion object {
        private const val TAG = "MainMenuFragment"
    }

    private lateinit var binding: FragmentMainMenuBinding
    private val mainMenuViewModel: MainMenuViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(requireActivity().application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
            if (it == null)
                findNavController().navigate(MainMenuFragmentDirections.actionMainMenuFragmentToSettingsFragment())
        })

        setListeners()

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.options_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.options_stats ->
                findNavController().navigate(MainMenuFragmentDirections.actionMainMenuFragmentToStatsFragment())
            R.id.options_settings ->
                findNavController().navigate(MainMenuFragmentDirections.actionMainMenuFragmentToSettingsFragment())
        }
        return super.onOptionsItemSelected(item)
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