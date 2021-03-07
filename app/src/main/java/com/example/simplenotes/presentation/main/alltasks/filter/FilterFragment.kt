package com.example.simplenotes.presentation.main.alltasks.filter

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentFilterBinding


class FilterFragment : Fragment() {

    private val binding: FragmentFilterBinding by lazy {
        FragmentFilterBinding.inflate(layoutInflater)
    }

    private val filterOptions: FilterOptions? by lazy {
        FilterFragmentArgs.fromBundle(requireArguments()).filterOptions
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
        filterOptions?.let {
            initViewWithReceivedFilter(it)
        } ?: initViewWithDefaultFilter()
    }

    private fun initViewWithDefaultFilter() {

    }

    private fun initViewWithReceivedFilter(filterOptions: FilterOptions) {

    }
}