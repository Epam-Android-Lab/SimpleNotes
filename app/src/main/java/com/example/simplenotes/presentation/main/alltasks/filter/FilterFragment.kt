package com.example.simplenotes.presentation.main.alltasks.filter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentFilterBinding
import com.example.simplenotes.presentation.main.alltasks.AllTasksFragmentArgs
import com.google.android.material.chip.Chip


class FilterFragment : Fragment() {

    private val binding: FragmentFilterBinding by lazy {
        FragmentFilterBinding.inflate(layoutInflater)
    }

    private  var filterOptions: FilterOptions? = null

    private val categoryId: String by lazy {
        FilterFragmentArgs.fromBundle(requireArguments()).categoryId
    }

    private val viewModel by viewModels<FilterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        filterOptions =  FilterFragmentArgs.fromBundle(requireArguments()).filterOptions
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.topAppBar
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment,appBarConfiguration)
        setHasOptionsMenu(true)

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

        binding.confirm.setOnClickListener {
            viewModel.confirm()
        }

        viewModel.filterOptions.observe(viewLifecycleOwner){
            when(it){
                is FilterViewModel.ConfirmState.Prepared -> {
                    filterOptions = it.filterOptions

                    val args = AllTasksFragmentArgs(filterOptions = filterOptions, categoryId = categoryId).toBundle()
                    findNavController().navigate(R.id.action_filterFragment_to_allTasksFragment, args)

                }
                else -> {}
            }
        }

        binding.remove.setOnClickListener {
            val args = AllTasksFragmentArgs(filterOptions = null, categoryId = categoryId).toBundle()
            findNavController().navigate(R.id.action_filterFragment_to_allTasksFragment, args)
        }

    }

    private fun initViewWithDefaultFilter() {
        viewModel.defaults()
    }

    private fun initViewWithReceivedFilter(filterOptions: FilterOptions) {
        viewModel.receives(filterOptions)
    }
}