/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.ui.aftermatch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.joonasniemi.wordquizproject.databinding.FragmentAfterMatchBinding

class AfterMatchFragment : Fragment() {
    private lateinit var binding: FragmentAfterMatchBinding

    private val viewModel: AfterMatchViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentAfterMatchBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setListeners()

        return binding.root
    }

    private fun setListeners(){
        binding.mainMenuButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }
}