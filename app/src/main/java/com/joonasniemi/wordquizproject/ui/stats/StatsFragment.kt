package com.joonasniemi.wordquizproject.ui.stats

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.database.words.WordDatabase
import com.joonasniemi.wordquizproject.databinding.FragmentStatsBinding
import com.joonasniemi.wordquizproject.ui.SharedViewModel
import com.joonasniemi.wordquizproject.ui.SharedViewModelFactory

class StatsFragment : Fragment() {
    companion object{
        private const val TAG = "StatsFragment"
    }

    private lateinit var binding: FragmentStatsBinding

    private lateinit var viewModel: StatsViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {

        val dataSource = WordDatabase.getInstance(requireActivity()).wordDatabaseDao
        viewModel = ViewModelProvider(this).get(StatsViewModel::class.java)

        binding = FragmentStatsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val adapter = WordListAdapter()
        binding.wordsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            this.adapter = adapter
        }

        binding.totalGuessesText.text = getString(R.string.total_guesses, 0)
        binding.totalRightGuessesText.text = getString(R.string.total_right_guesses, 0)
        binding.totalWordsText.text = getString(R.string.words, 0)

        return binding.root
    }
}