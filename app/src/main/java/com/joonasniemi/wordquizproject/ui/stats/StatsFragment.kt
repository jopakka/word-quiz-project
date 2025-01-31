/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.joonasniemi.wordquizproject.databinding.FragmentStatsBinding
import com.joonasniemi.wordquizproject.ui.SharedViewModel
import com.joonasniemi.wordquizproject.ui.SharedViewModelFactory
import com.joonasniemi.wordquizproject.ui.Status

class StatsFragment : Fragment() {
    /**
     * Layouts bindings
     */
    private lateinit var binding: FragmentStatsBinding

    /**
     * Creates viewModel for fragment
     */
    private val statsViewModel: StatsViewModel by viewModels {
        StatsViewModelFactory(requireActivity().application)
    }

    /**
     * Gets sharedViewModel from activityViewModels
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentStatsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = statsViewModel

        setAdapter()

        return binding.root
    }

    /**
     * Sets word recycler view adapter ready
     */
    private fun setAdapter(){
        val adapter = WordListAdapter()

        var userAnswerLanguage = ""

        sharedViewModel.user.observe(viewLifecycleOwner, {
            userAnswerLanguage = it.answerLanguage
            statsViewModel.initGuessedWords(it.language, it.answerLanguage)
        })

        statsViewModel.status.observe(viewLifecycleOwner, {
            if(it == Status.DONE)
                adapter.guessedWords = statsViewModel.guessedWords
        })

        sharedViewModel.allWords.observe(viewLifecycleOwner, {
            val setOfWords = it.filter { w -> w.lang == userAnswerLanguage }.toSet()
            statsViewModel.wordCount.value = setOfWords.size
            adapter.wordSet = setOfWords

            binding.wordsRecyclerView.apply {
                layoutManager = LinearLayoutManager(context)
                this.adapter = adapter
            }
        })
    }
}