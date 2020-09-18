package com.joonasniemi.wordquizproject.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.joonasniemi.wordquizproject.databinding.FragmentGameBinding

class GameFragment : Fragment() {
    companion object {
        private const val TAG = "GameFragment"
    }

    private lateinit var binding: FragmentGameBinding

    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this, GameViewModelFactory(arguments)).get(GameViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentGameBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }
}