package com.example.simplenotes.presentation.main.alltasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentAllTasksBinding
import com.example.simplenotes.presentation.main.TaskShowFragmentArgs
import com.example.simplenotes.presentation.main.alltasks.filter.FilterFragmentArgs
import com.example.simplenotes.presentation.main.alltasks.filter.FilterOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllTasksFragment : Fragment() {

    private val binding: FragmentAllTasksBinding by lazy {
        FragmentAllTasksBinding.inflate(layoutInflater)
    }

    @ExperimentalStdlibApi
    private val viewModel:AllTasksViewModel by viewModel()

    private val categoryId: String by lazy {
        AllTasksFragmentArgs.fromBundle(requireArguments()).categoryId
    }

    private val options = mutableListOf<String>()

    private var filterOptions: FilterOptions? = null

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filterOptions =  AllTasksFragmentArgs.fromBundle(requireArguments()).filterOptions
        viewModel.getData(categoryId, filterOptions)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    @ExperimentalStdlibApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = binding.topAppBar
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment,appBarConfiguration)
        setHasOptionsMenu(true)
        toolbar.inflateMenu(R.menu.all_tasks_fragment_menu)
        toolbar.setOnMenuItemClickListener {
            if(it.itemId == R.id.see_filters){
                val args = FilterFragmentArgs(
                    filterOptions = filterOptions,
                    categoryId = categoryId
                ).toBundle()
                findNavController().navigate(R.id.action_allTasksFragment_to_filterFragment, args)
            }
            return@setOnMenuItemClickListener false
        }

        val adapter = TaskAdapter(requireContext(), { status: Boolean, id: String ->
            viewModel.updateStatus(status, id)
        }, {
            val args = TaskShowFragmentArgs(id = it, deadlineNotifId = it.hashCode(), reminderNotifId = (it.hashCode() +1)).toBundle()
            findNavController().navigate(R.id.action_allTasksFragment_to_taskShowFragment, args)
        })

        binding.allRecycler.adapter = adapter
        viewModel.listOfTasks.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_allTasksFragment_to_taskFragment)
        }


        val bottomBehavior = BottomSheetBehavior.from(binding.bottomSort.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        viewModel.getOptions(requireContext()).also {
            viewModel.listOfOptions.observe(viewLifecycleOwner) {

                val listAdapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    it
                )
                binding.bottomSort.listOptions.adapter = listAdapter

                viewModel.setActiveSortOption(0)

                binding.bottomSort.listOptions.setOnItemClickListener { _, _, position, _ ->
                    viewModel.setActiveSortOption(position)
                    bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN
                }
            }
        }

        binding.sort.setOnClickListener {
            if (bottomBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
                bottomBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            } else {
                bottomBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            }
        }

        viewModel.activeSort.observe(viewLifecycleOwner) {
            binding.sort.text = it
            binding.bottomSort.listOptions.deferNotifyDataSetChanged()
        }
    }
}