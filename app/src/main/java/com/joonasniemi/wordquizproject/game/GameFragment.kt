package com.joonasniemi.wordquizproject.game

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.databinding.FragmentGameBinding
import com.joonasniemi.wordquizproject.mainmenu.MainMenuViewModel
import com.joonasniemi.wordquizproject.mainmenu.WordsApiStatus
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.network.WordList

class GameFragment : Fragment() {
    companion object {
        private const val TAG = "GameFragment"
    }

    private lateinit var binding: FragmentGameBinding

    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this).get(GameViewModel::class.java)
    }

    private lateinit var wordList: Set<Word>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentGameBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        wordList = (arguments?.get("wordList") as WordList).list

        return inflater.inflate(R.layout.fragment_game, container, false)
    }
}