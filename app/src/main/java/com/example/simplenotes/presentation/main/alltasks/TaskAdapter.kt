package com.example.simplenotes.presentation.main.alltasks

import android.animation.LayoutTransition
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenotes.R
import com.example.simplenotes.databinding.RecyclerAllTasksItemBinding
import com.example.simplenotes.domain.entities.Task


class TaskAdapter : ListAdapter<Task, TaskAdapter.Holder>(DiffCallback) {

    class Holder(private val binding: RecyclerAllTasksItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        constructor(parent: ViewGroup) : this(
            RecyclerAllTasksItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

        companion object {
            const val NUMBER_OF_LINES = 3
        }

        fun bind(task: Task) {
            var collapsed = false
            binding.apply {
                title.text = task.title
                description.text = task.description
                category.text = task.category
                priority.text = task.priority.toString()

                /*
                todo: 1. add time converter
                        2. check for null
                */
                deadline.text = task.deadline.toString()
                notification.text = task.notification.toString()

                title.setOnClickListener {

                    //todo: change status
                    if (title.isChecked) {

                    } else {

                    }
                }

                edit.setOnClickListener {
                    editTask(task)
                }

                //todo: Check length of text
                description.setOnClickListener {
                    if (!collapsed) {
                        description.maxLines = Int.MAX_VALUE
                        collapsed = true
                        collapse.setImageResource(R.drawable.ic_expand_less_black_18dp)
                    } else {
                        description.maxLines = NUMBER_OF_LINES
                        collapsed = false
                        collapse.setImageResource(R.drawable.ic_expand_more_black_18dp)
                    }
                }

            }
        }

        private fun editTask(task: Task) {
            TODO()
        }
    }


    object DiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(parent)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }

}