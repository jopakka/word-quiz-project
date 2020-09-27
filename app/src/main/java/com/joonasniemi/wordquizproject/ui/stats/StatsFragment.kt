package com.joonasniemi.wordquizproject.ui.stats

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.joonasniemi.wordquizproject.database.user.User
import com.joonasniemi.wordquizproject.database.user.UserDatabase
import com.joonasniemi.wordquizproject.database.words.WordDatabase
import com.joonasniemi.wordquizproject.databinding.FragmentStatsBinding
import com.joonasniemi.wordquizproject.network.Word
import com.joonasniemi.wordquizproject.ui.SharedViewModel
import com.joonasniemi.wordquizproject.ui.SharedViewModelFactory
import kotlinx.coroutines.launch

class StatsFragment : Fragment() {
    companion object{
        private const val TAG = "StatsFragment"
    }

    private lateinit var binding: FragmentStatsBinding

    private val statsViewModel: StatsViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        binding = FragmentStatsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = statsViewModel

        val adapter = WordListAdapter()

        var userLanguage = ""
        var userAnswerLanguage = ""

        sharedViewModel.user.observe(viewLifecycleOwner, {
            statsViewModel.totalGuesses.value = it.totalGuesses
            statsViewModel.rightGuesses.value = it.rightGuesses
            userLanguage = it.language
            userAnswerLanguage = it.answerLanguage
            adapter.user = it
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

        return binding.root
    }
}