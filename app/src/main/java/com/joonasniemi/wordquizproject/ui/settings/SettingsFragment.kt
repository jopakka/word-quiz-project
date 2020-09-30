/*
 * Joonas Niemi
 * 1909887
 */

package com.joonasniemi.wordquizproject.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.joonasniemi.wordquizproject.R
import com.joonasniemi.wordquizproject.database.user.User
import com.joonasniemi.wordquizproject.database.user.UserDatabase
import com.joonasniemi.wordquizproject.databinding.FragmentSettingsBinding
import com.joonasniemi.wordquizproject.ui.SharedViewModel
import com.joonasniemi.wordquizproject.ui.SharedViewModelFactory
import com.joonasniemi.wordquizproject.ui.Status
import com.joonasniemi.wordquizproject.ui.mainmenu.LanguagesSpinnerAdapter

class SettingsFragment : Fragment() {
    /**
     * Layouts bindings
     */
    private lateinit var binding: FragmentSettingsBinding

    /**
     * Creates viewModel for fragment
     */
    private val settingsViewModel: SettingsViewModel by viewModels {
        val database = UserDatabase.getInstance(requireContext()).userDatabaseDao
        SettingsViewModelFactory(database)
    }

    /**
     * Gets sharedViewModel from activityViewModels
     */
    private val sharedViewModel: SharedViewModel by activityViewModels {
        SharedViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentSettingsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel
        binding.ownViewModel = settingsViewModel

        initSpinner(binding.languagesSpinner)
        initSpinner(binding.answerLanguagesSpinner)
        setListeners()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        setEverySpinnerValue()

        sharedViewModel.user.observe(viewLifecycleOwner, {
            if(it != null){
                if(settingsViewModel.selectedLanguage.value == null
                    && settingsViewModel.selectedAnswerLanguage.value == null){
                    settingsViewModel.selectedLanguage.value = it.language
                    settingsViewModel.selectedAnswerLanguage.value = it.answerLanguage
                    setEverySpinnerValue()
                }
            }
        })
    }

    /**
     * Set placeholder item for spinner language list.
     * Adds all languages from sharedViewModels allWords LiveData list
     */
    private fun initSpinner(spinner: Spinner){
        val list = mutableListOf(spinner.context.getString(R.string.select_language))
        list.addAll(sharedViewModel.allWords.value?.map { w -> w.lang }?.sorted()?.distinct() ?: emptyList())
        spinner.isEnabled = list.size > 1
        spinner.adapter = LanguagesSpinnerAdapter(spinner.context, list)
    }

    private fun setEverySpinnerValue(){
        setSpinnerValue(binding.languagesSpinner, settingsViewModel.selectedLanguage.value)
        setSpinnerValue(binding.answerLanguagesSpinner, settingsViewModel.selectedAnswerLanguage.value)
    }

    /**
     * Sets [spinner] selected item to correspond [language]
     */
    private fun setSpinnerValue(spinner: Spinner, language: String?) {
        val pos = (binding.languagesSpinner.adapter as LanguagesSpinnerAdapter).getPosition(language)
        spinner.setSelection(pos)
    }

    /**
     * Initialize listeners to different elements
     */
    private fun setListeners() {
        /**
         * Save buttons onCLickListener
         */
        binding.saveButton.setOnClickListener {
            settingsViewModel.insert(
                User(
                    binding.languagesSpinner.selectedItem.toString(),
                    binding.answerLanguagesSpinner.selectedItem.toString()
                )
            )
            settingsViewModel.status.observe(viewLifecycleOwner, {
                if(it == Status.DONE)
                    findNavController()
                        .navigate(SettingsFragmentDirections.actionSettingsFragmentToMainMenuFragment())
            })
        }

        /**
         * Current language spinner onItemSelectedListener
         */
        binding.languagesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    settingsViewModel.selectedLanguage.value = parent?.selectedItem as String
                    binding.saveButton.isEnabled = checkSpinnersNotEqual() && checkSpinnersNotZero()
                    if (!checkSpinnersNotEqual())
                        binding.answerLanguagesSpinner.setSelection(0)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    parent?.setSelection(0)
                }
            }

        /**
         * Learning language spinner onItemSelectedSpinner
         */
        binding.answerLanguagesSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    settingsViewModel.selectedAnswerLanguage.value = parent?.selectedItem as String
                    binding.saveButton.isEnabled = checkSpinnersNotEqual() && checkSpinnersNotZero()
                    if (!checkSpinnersNotEqual())
                        binding.languagesSpinner.setSelection(0)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    parent?.setSelection(0)
                }
            }
    }

    /**
     * Checks that are two language spinners equals
     */
    private fun checkSpinnersNotEqual(): Boolean {
        return binding.languagesSpinner.selectedItem != binding.answerLanguagesSpinner.selectedItem
    }

    /**
     * Checks that two language spinners values aren't 0
     */
    private fun checkSpinnersNotZero(): Boolean {
        return binding.languagesSpinner.selectedItemId != 0L
                && binding.answerLanguagesSpinner.selectedItemId != 0L
    }

}