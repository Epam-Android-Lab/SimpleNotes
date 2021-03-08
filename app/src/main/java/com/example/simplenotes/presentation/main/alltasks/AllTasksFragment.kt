package com.example.simplenotes.presentation.main.alltasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentAllTasksBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel

class AllTasksFragment : Fragment() {

    private val binding: FragmentAllTasksBinding by lazy {
        FragmentAllTasksBinding.inflate(layoutInflater)
    }

    private val viewModel: AllTasksViewModel by viewModel()

    private val options = mutableListOf<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryId = AllTasksFragmentArgs.fromBundle(requireArguments()).categoryId

        viewModel.getData(categoryId)

        val adapter = TaskAdapter(requireContext()) { status: Boolean, id: String ->
            viewModel.updateStatus(status, id)
        }

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