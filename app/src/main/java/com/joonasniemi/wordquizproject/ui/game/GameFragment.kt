package com.joonasniemi.wordquizproject.ui.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.databinding.FragmentGameBinding
import kotlinx.android.synthetic.main.fragment_game.view.*

class GameFragment : Fragment() {
    companion object {
        private const val TAG = "GameFragment"
    }

    private lateinit var binding: FragmentGameBinding
    
    /**
     * Creates viewModel for fragment
     */
    private val viewModel: GameViewModel by lazy {
        ViewModelProvider(this, GameViewModelFactory(arguments)).get(GameViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentGameBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.game = this
        setListeners()

        return binding.root
    }

    private fun setListeners(){
        binding.submitButton.setOnClickListener {view ->
            val checkedId = binding.answerRadioGroup.checkedRadioButtonId
            if(checkedId != -1){
                var answerIndex = 0
                when(checkedId){
                    R.id.answer_radio_2 -> answerIndex = 1
                    R.id.answer_radio_3 -> answerIndex = 2
                    R.id.answer_radio_4 -> answerIndex = 3
                }

            }
        }
    }
}