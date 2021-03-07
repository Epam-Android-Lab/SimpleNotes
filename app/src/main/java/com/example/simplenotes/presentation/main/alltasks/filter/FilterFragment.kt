package com.example.simplenotes.presentation.main.alltasks.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentFilterBinding
import com.google.android.material.chip.Chip


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
                val chip =
                    layoutInflater.inflate(R.layout.checked_chip, binding.categories, false) as Chip
                chip.text = it.name
                chip.setOnCheckedChangeListener { _, isChecked ->
                    viewModel.updateCheckedList(it, isChecked)
                }
                binding.categories.addView(chip)
            }
        }

        viewModel.priority.observe(viewLifecycleOwner) {
            binding.sliderPriority.value = it.toFloat()
        }

        binding.sliderPriority.addOnChangeListener { _, value, _ ->
            viewModel.updatePriority(value = value.toInt())
        }

        viewModel.isDone.observe(viewLifecycleOwner) {
            if (it) {
                binding.done.isChecked = true
                binding.planned.isChecked = false
            } else {
                binding.done.isChecked = false
                binding.planned.isChecked = true
            }
        }

        binding.done.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) viewModel.updateIsDone(true)
        }

        binding.planned.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) viewModel.updateIsDone(false)
        }


    }

    private fun initViewWithDefaultFilter() {
        viewModel.defaults()
    }

    private fun initViewWithReceivedFilter(filterOptions: FilterOptions) {

    }
}