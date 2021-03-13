package com.example.simplenotes.presentation.main.alltasks.filter

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentFilterBinding
import com.example.simplenotes.presentation.main.MainActivity
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

    private var fromLibrary = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fromLibrary = FilterFragmentArgs.fromBundle(requireArguments()).fromLibrary
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

        (activity as MainActivity).setSupportActionBar(toolbar)

        toolbar.setOnMenuItemClickListener {
            if(it.itemId == R.id.clear) navigateBackToAllTasksWith()
            return@setOnMenuItemClickListener false
        }

        if(!fromLibrary) {
            binding.categoriesContainer.visibility = View.GONE
        } else {
            binding.categoriesContainer.visibility = View.VISIBLE
        }

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
                    navigateBackToAllTasksWith(filterOptions = filterOptions)
                }
                else -> {}
            }
        }
    }

    private fun navigateBackToAllTasksWith(filterOptions: FilterOptions? = null) {
        val args = AllTasksFragmentArgs(
            filterOptions = filterOptions,
            categoryId = categoryId,
            fromLibrary = fromLibrary
        ).toBundle()
        findNavController().navigate(R.id.action_filterFragment_to_allTasksFragment, args)
    }

    private fun initViewWithDefaultFilter() {
        viewModel.defaults()
    }

    private fun initViewWithReceivedFilter(filterOptions: FilterOptions) {
        viewModel.receives(filterOptions)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) (activity as MainActivity).onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}