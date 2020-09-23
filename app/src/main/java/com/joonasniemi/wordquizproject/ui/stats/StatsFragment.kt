package com.joonasniemi.wordquizproject.ui.stats

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.databinding.FragmentMainMenuBinding
import com.joonasniemi.wordquizproject.databinding.FragmentStatsBinding
import com.joonasniemi.wordquizproject.network.WordsRepository

class StatsFragment : Fragment() {

    private lateinit var binding: FragmentStatsBinding

    private val viewModel: StatsViewModel by lazy {
        ViewModelProvider(this).get(StatsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
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

        viewModel.wordSet.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.data = it.shuffled().toSet()
                binding.totalWordsText.text = getString(R.string.words, it.size)
            }
        })

        return binding.root
    }
}