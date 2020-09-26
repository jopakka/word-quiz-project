package com.joonasniemi.wordquizproject.ui.aftermatch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.joonasniemi.wordquizproject.databinding.FragmentAfterMatchBinding
import com.joonasniemi.wordquizproject.utils.AfterMatchArguments

class AfterMatchFragment : Fragment() {
    companion object {
        private const val TAG = "AfterMatchFragment"
    }

    private lateinit var binding: FragmentAfterMatchBinding
    private lateinit var args: AfterMatchArguments

    private val viewModel: AfterMatchViewModel by lazy {
        args = (arguments?.get("afterMatchArguments") as AfterMatchArguments)
        ViewModelProvider(this, AfterMatchViewModelFactory(args)).get(AfterMatchViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAfterMatchBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }
}