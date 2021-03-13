package com.example.simplenotes.presentation.main

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenotes.R
import com.example.simplenotes.databinding.RecyclerAllTasksItemBinding
import com.example.simplenotes.databinding.TaskLatestItemBinding
import com.example.simplenotes.domain.entities.Task
import java.text.SimpleDateFormat
import java.util.*

class LatestTasksAdapter @ExperimentalStdlibApi constructor(
    private val context: Context,
    private val fragment: MainScreenFragment
) : ListAdapter<Task, LatestTasksAdapter.LatestTasksViewHolder>(DiffCallback) {

    inner class LatestTasksViewHolder(private val binding: RecyclerAllTasksItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        constructor(parent: ViewGroup) : this(
            RecyclerAllTasksItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

        fun bind(
            task: Task,
        ) {
            binding.apply {
                textTitle.text = task.title

                priority.text = task.priority.toString()

                cardPriority.backgroundTintList = ColorStateList.valueOf(
                    when (task.priority) {
                        1 -> context.resources.getColor(R.color.priority_1)
                        2 -> context.resources.getColor(R.color.priority_2)
                        3 -> context.resources.getColor(R.color.priority_3)
                        4 -> context.resources.getColor(R.color.priority_4)
                        5 -> context.resources.getColor(R.color.priority_5)
                        else -> context.resources.getColor(R.color.priority_3)
                    }
                )

                task.deadline?.let {
                    deadline.text = convertLongToTime(it)
                }

                title.isChecked = task.status

                if (title.isChecked) {
                    setThatTaskCompleted(context)
                } else {
                    mayBeNeedToShowOverdue(task, context)
                }
                val args = TaskShowFragmentArgs(
                    id = task.id,
                    deadlineNotifId = task.id.hashCode(),
                    reminderNotifId = (task.id.hashCode() + 1)
                ).toBundle()
                root.setOnClickListener {
                    fragment.findNavController().navigate(
                        R.id.action_mainScreenFragment_to_taskShowFragment,
                        args
                    )
                }

            }
        }

        private fun RecyclerAllTasksItemBinding.mayBeNeedToShowOverdue(
            task: Task,
            context: Context
        ) {
            if (task.deadline != null && currentTimeToLong() > task.deadline!!) {
                indicator.visibility = View.VISIBLE
                indicator.setBackgroundColor(context.resources.getColor(R.color.red))
                deadline.setTextColor(context.resources.getColor(R.color.red))
            } else {
                indicator.visibility = View.INVISIBLE

                deadline.setTextColor(TextView(context).textColors.defaultColor)
            }
        }

        private fun RecyclerAllTasksItemBinding.setThatTaskCompleted(
            context: Context
        ) {
            indicator.visibility = View.VISIBLE
            indicator.setBackgroundColor(context.resources.getColor(R.color.green))
            deadline.setTextColor(context.resources.getColor(R.color.green))
        }


        private fun convertLongToTime(time: Long) =
            SimpleDateFormat("yyyy.MM.dd HH:mm").format(Date(time))

        private fun currentTimeToLong() = System.currentTimeMillis()
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