package com.example.simplenotes.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenotes.databinding.CategoryFolderItemBinding
import com.example.simplenotes.domain.entities.Category

class MyCategoriesAdapter :
    ListAdapter<Category, MyCategoriesAdapter.MyCategoriesViewHolder>(DiffCallback) {

    class MyCategoriesViewHolder(private val binding: CategoryFolderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        constructor(parent: ViewGroup) : this(
            CategoryFolderItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

        fun bind(category: Category) {
            binding.categoryName.text = category.name
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