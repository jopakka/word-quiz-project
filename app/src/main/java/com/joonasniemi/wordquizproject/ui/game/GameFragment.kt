package com.joonasniemi.wordquizproject.ui.game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.database.user.UserDatabase
import com.joonasniemi.wordquizproject.database.words.WordDatabase
import com.joonasniemi.wordquizproject.databinding.FragmentGameBinding
import com.joonasniemi.wordquizproject.utils.AfterMatchArguments
import com.joonasniemi.wordquizproject.utils.GameArguments
import kotlinx.coroutines.launch

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
        binding.viewModel = viewModel
        setGame()

        return binding.root
    }

    private fun setGame() {
        val args = arguments?.get("gameArguments") as GameArguments
        viewModel.initGame(args.words, args.answerLanguage)

        binding.submitButton.setOnClickListener {
            val checkedId = binding.answerRadioGroup.checkedRadioButtonId
            if (checkedId != -1) {
                val answerIndex = when (checkedId) {
                    R.id.answer_radio_2 -> 1
                    R.id.answer_radio_3 -> 2
                    R.id.answer_radio_4 -> 3
                    else -> 0
                }

                val distance = checkDistance(
                    viewModel.answers.value
                        ?.get(answerIndex), args.answerLanguage
                )

                if (distance == 0) {
                    viewModel.currentWord.value?.let {
                        viewModel.userCorrectAnswers.add(it)
                    }

                    // TODO("Show good job")

                    viewLifecycleOwner.lifecycleScope.launch {
                        try {
                            viewModel.currentWord.value?.let {
                                viewModel.updateRightGuessed(
                                    it.id,
                                    viewModel.language,
                                    viewModel.answerLanguage
                                )
                            }
                        } catch (e: Exception) {
                            Log.e(TAG, e.message.toString())
                        }
                    }.invokeOnCompletion {
                        nextQuestion(args.words.size)
                    }
                } else {
                    // TODO("Show it's false answer")
                    nextQuestion(args.words.size)
                }
            }
        }
    }

    private fun nextQuestion(maxQuestions: Int) {
        viewModel.questionIndex++
        if (viewModel.questionIndex < maxQuestions) {
            binding.answerRadioGroup.clearCheck()
            viewModel.setQuestion()
            binding.invalidateAll()
        } else {
            findNavController().navigate(
                GameFragmentDirections
                    .actionGameFragmentToAfterMatchFragment(
                        AfterMatchArguments(viewModel.userCorrectAnswers, maxQuestions)
                    )
            )
        }
    }

    private fun checkDistance(answer: String?, language: String): Int? {
        return viewModel.currentWord.value?.translations
            ?.first { it.lang == language }
            ?.editDistance(answer)
    }
}