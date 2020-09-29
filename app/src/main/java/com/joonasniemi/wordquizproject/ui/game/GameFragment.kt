/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.database.user.UserDatabase
import com.joonasniemi.wordquizproject.database.words.WordDatabase
import com.joonasniemi.wordquizproject.databinding.FragmentGameBinding
import com.joonasniemi.wordquizproject.game.Quiz
import com.joonasniemi.wordquizproject.utils.AfterMatchArguments

class GameFragment : Fragment() {
    companion object {
        private const val TAG = "GameFragment"
    }

    private lateinit var binding: FragmentGameBinding

    /**
     * Creates viewModel for fragment
     */
    private val viewModel: GameViewModel by viewModels {
        val wordDatabaseDao = WordDatabase.getInstance(requireActivity()).wordDatabaseDao
        val userDatabaseDao = UserDatabase.getInstance(requireActivity()).userDatabaseDao
        GameViewModelFactory(wordDatabaseDao, userDatabaseDao)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentGameBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.quiz = Quiz
        Quiz.setQuestion()

        setListeners()

        return binding.root
    }

    private fun setListeners() {
        binding.submitButton.setOnClickListener {
            val checkedId = binding.answerRadioGroup.checkedRadioButtonId
            if (checkedId != -1) {
                val answerIndex = when (checkedId) {
                    R.id.answer_radio_2 -> 1
                    R.id.answer_radio_3 -> 2
                    R.id.answer_radio_4 -> 3
                    else -> 0
                }

                if (Quiz.checkAnswer(answerIndex)) {
                    // TODO("Show good job")
                    viewModel.correctAnswer{ nextQuestion() }
                } else {
                    // TODO("Show it's false answer")
                    nextQuestion()
                }
            }
        }
    }

    private fun nextQuestion(){
        if (Quiz.nextQuestion()) {
            binding.answerRadioGroup.clearCheck()
            viewModel.setQuestion()
            binding.invalidateAll()
        } else {
            findNavController().navigate(
                GameFragmentDirections
                    .actionGameFragmentToAfterMatchFragment())
        }
    }
}