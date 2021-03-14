package com.example.simplenotes.presentation.main.alltasks

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentAllTasksBinding
import com.example.simplenotes.presentation.main.MainActivity
import com.example.simplenotes.presentation.main.TaskShowFragmentArgs
import com.example.simplenotes.presentation.main.alltasks.filter.FilterFragmentArgs
import com.example.simplenotes.presentation.main.alltasks.filter.FilterOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel


class AllTasksFragment : Fragment(){

    private val binding: FragmentAllTasksBinding by lazy {
        FragmentAllTasksBinding.inflate(layoutInflater)
    }

    @ExperimentalStdlibApi
    private val viewModel: AllTasksViewModel by viewModel()

    private val categoryId: String by lazy {
        AllTasksFragmentArgs.fromBundle(requireArguments()).categoryId
    }

    private val options = mutableListOf<String>()

    private var filterOptions: FilterOptions? = null

    private var fromLibrary: Boolean = false

    @ExperimentalStdlibApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        filterOptions = AllTasksFragmentArgs.fromBundle(requireArguments()).filterOptions
        fromLibrary = AllTasksFragmentArgs.fromBundle(requireArguments()).fromLibrary

        if(fromLibrary) viewModel.getTasksByCategory(categoryId, filterOptions) else viewModel.getData(categoryId, filterOptions)
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
        NavigationUI.setupWithNavController(toolbar, navHostFragment, appBarConfiguration)
        setHasOptionsMenu(true)

        (activity as MainActivity).setSupportActionBar(toolbar)

        toolbar.setOnMenuItemClickListener {
            if (it.itemId == R.id.see_filters) {
                val args = FilterFragmentArgs(
                    filterOptions = filterOptions,
                    categoryId = categoryId,
                    fromLibrary = fromLibrary
                ).toBundle()
                findNavController().navigate(R.id.action_allTasksFragment_to_filterFragment, args)
            }
            return@setOnMenuItemClickListener false
        }

        toolbar.title = categoryId

        val adapter = TaskAdapter(requireContext(), { status: Boolean, id: String ->
            viewModel.updateStatus(status, id)
        }, {
            val args = TaskShowFragmentArgs(
                id = it,
                deadlineNotifId = it.hashCode(),
                reminderNotifId = (it.hashCode() + 1)
            ).toBundle()
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.all_tasks_fragment_menu, menu)
    }

    @ExperimentalStdlibApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) (activity as MainActivity).onBackPressed()
        return super.onOptionsItemSelected(item)
    }
}