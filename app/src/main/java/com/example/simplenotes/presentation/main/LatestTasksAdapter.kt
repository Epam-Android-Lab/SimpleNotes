package com.example.simplenotes.presentation.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenotes.databinding.TaskLatestItemBinding
import com.example.simplenotes.domain.entities.Task

class LatestTasksAdapter :
    ListAdapter<Task, LatestTasksAdapter.LatestTasksViewHolder>(DiffCallback) {

    class LatestTasksViewHolder(private val binding: TaskLatestItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        constructor(parent: ViewGroup) : this(
            TaskLatestItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

        fun bind(item: Task) {
            binding.latestTaskItemTitle.text = item.title
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LatestTasksViewHolder {
        return LatestTasksViewHolder(parent)
    }

    override fun onBindViewHolder(holder: LatestTasksViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    object DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }
}