package com.example.simplenotes.presentation.main.alltasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.simplenotes.databinding.FragmentAllTasksBinding


class AllTasksFragment : Fragment() {

    private val binding: FragmentAllTasksBinding by lazy {
        FragmentAllTasksBinding.inflate(layoutInflater)
    }

    @ExperimentalStdlibApi
    private val viewModel by viewModels<AllTasksViewModel>{
        AllTasksViewModelFactory(categoryId)
    }

    private val categoryId: String by lazy {
        AllTasksFragmentArgs.fromBundle(requireArguments()).categoryId
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

        val adapter = TaskAdapter()

        binding.allRecycler.adapter = adapter
        viewModel.listOfTasks.observe(viewLifecycleOwner){
            adapter.submitList(it)
        }
    }

    class AllTasksViewModelFactory(private val categoryId: String): ViewModelProvider.NewInstanceFactory() {
        @ExperimentalStdlibApi
        override fun <T : ViewModel?> create(modelClass: Class<T>): T = AllTasksViewModel(categoryId) as T
    }
}