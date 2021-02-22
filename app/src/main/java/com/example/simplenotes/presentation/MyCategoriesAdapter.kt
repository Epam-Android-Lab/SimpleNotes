package com.example.simplenotes.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenotes.databinding.CategoryFolderItemBinding
import com.example.simplenotes.domain.entities.Category

class MyCategoriesAdapter : RecyclerView.Adapter<MyCategoriesAdapter.MyCategoriesViewHolder>() {

    var list = emptyList<Category>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    class MyCategoriesViewHolder(private val binding: CategoryFolderItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        constructor(parent: ViewGroup) : this(
                CategoryFolderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

        fun bind(category: Category) {
            binding.categoryName.text = category.name
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyCategoriesViewHolder {
        return MyCategoriesViewHolder(parent)
    }

    override fun onBindViewHolder(holder: MyCategoriesViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size
}