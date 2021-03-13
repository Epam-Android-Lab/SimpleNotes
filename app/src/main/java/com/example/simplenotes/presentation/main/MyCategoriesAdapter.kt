package com.example.simplenotes.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenotes.R
import com.example.simplenotes.databinding.CategoryFolderItemBinding
import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.presentation.main.alltasks.AllTasksFragmentArgs

class MyCategoriesAdapter @ExperimentalStdlibApi constructor(
    val fragment: MainScreenFragment
) : ListAdapter<Category, MyCategoriesAdapter.MyCategoriesViewHolder>(DiffCallback) {


     inner class MyCategoriesViewHolder(private val binding: CategoryFolderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        constructor(parent: ViewGroup) : this(
            CategoryFolderItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

        fun bind(category: Category) {
            val name = category.name
            binding.categoryName.text = name
            binding.clMyCategoryItem.setOnClickListener {
                fragment.findNavController().navigate(
                    R.id.action_mainScreenFragment_to_allTasksFragment,
                    AllTasksFragmentArgs(name).toBundle()
                )
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoriesViewHolder {
        return MyCategoriesViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MyCategoriesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
            return oldItem == newItem
        }
    }

}