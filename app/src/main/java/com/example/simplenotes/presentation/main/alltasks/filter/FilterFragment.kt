package com.example.simplenotes.presentation.main.alltasks.filter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.forEach
import androidx.fragment.app.viewModels
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentFilterBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipDrawable


class FilterFragment : Fragment() {

    private val binding: FragmentFilterBinding by lazy {
        FragmentFilterBinding.inflate(layoutInflater)
    }

    private val filterOptions: FilterOptions? by lazy {
        FilterFragmentArgs.fromBundle(requireArguments()).filterOptions
    }

    private val viewModel by viewModels<FilterViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        filterOptions?.let {
            initViewWithReceivedFilter(it)
        } ?: initViewWithDefaultFilter()


        viewModel.allCategories.observe(viewLifecycleOwner) { allCategories ->
            allCategories.forEach {
                val chip = layoutInflater.inflate(R.layout.checked_chip, binding.categories, false) as Chip
                chip.text = it.name
                chip.setOnCheckedChangeListener { buttonView, isChecked ->
                    Log.d("KEK", "chip with text ${buttonView.text} check status is $isChecked")
                }
                binding.categories.addView(chip)
            }
        }
    }

    private fun initViewWithDefaultFilter() {
        viewModel.defaults()
    }

    private fun initViewWithReceivedFilter(filterOptions: FilterOptions) {

    }
}