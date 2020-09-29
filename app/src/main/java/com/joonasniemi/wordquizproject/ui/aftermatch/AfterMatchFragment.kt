/*
 * Joonas Niemi
 * 1908997
 */

package com.joonasniemi.wordquizproject.ui.aftermatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.joonasniemi.wordquizproject.databinding.FragmentAfterMatchBinding
import com.joonasniemi.wordquizproject.utils.AfterMatchArguments

class AfterMatchFragment : Fragment() {
    private lateinit var binding: FragmentAfterMatchBinding
    private lateinit var args: AfterMatchArguments

    private val viewModel: AfterMatchViewModel by viewModels {
        args = (arguments?.get("afterMatchArguments") as AfterMatchArguments)
        AfterMatchViewModelFactory(args)
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