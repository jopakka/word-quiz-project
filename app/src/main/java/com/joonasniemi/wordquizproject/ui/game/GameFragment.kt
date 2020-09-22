package com.joonasniemi.wordquizproject.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.databinding.FragmentGameBinding
import com.joonasniemi.wordquizproject.network.AfterMatchArguments
import com.joonasniemi.wordquizproject.network.GameArguments

class GameFragment : Fragment() {
    companion object {
        private const val TAG = "GameFragment"
    }

    private lateinit var binding: FragmentGameBinding
    
    /**
     * Creates viewModel for fragment
     */
    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this).get(GameViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentGameBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        setGame()

        return binding.root
    }

    private fun setGame(){
        val args = (arguments?.get("gameArguments") as GameArguments)
        viewModel.initGame(args.words, args.answerLanguage, args.gameType)
        viewModel.setQuestion()

        binding.submitButton.setOnClickListener {view ->
            when(args.gameType){
                GameType.MULTI -> {
                    val checkedId = binding.answerRadioGroup.checkedRadioButtonId
                    if(checkedId != -1){
                        val answerIndex = when(checkedId){
                            R.id.answer_radio_2 -> 1
                            R.id.answer_radio_3 -> 2
                            R.id.answer_radio_4 -> 3
                            else -> 0
                        }

                        val distance = checkDistance(viewModel.answers.value?.get(answerIndex),
                            args.answerLanguage)

                        if(distance == 0){
                            viewModel.currentWord.value?.let {
                                viewModel.userCorrectAnswers.add(it)
                            }

                            // TODO("Show good job")
                        } else {
                            // TODO("Show it's false answer")
                        }

                        viewModel.questionIndex++
                        if(viewModel.questionIndex < args.words.size){
                            binding.answerRadioGroup.clearCheck()
                            viewModel.setQuestion()
                            binding.invalidateAll()
                        } else {
                            findNavController().navigate(
                                GameFragmentDirections
                                    .actionGameFragmentToAfterMatchFragment(
                                        AfterMatchArguments(viewModel.userCorrectAnswers, args.words.size)
                                    )
                            )
                        }
                    }
                }
                GameType.TEXT -> {

                }
            }
        }
    }

    private fun checkDistance(answer: String?, language: String): Int? {
        return viewModel.currentWord.value?.translations
            ?.first { it.lang == language }
            ?.editDistance(answer)
    }
}