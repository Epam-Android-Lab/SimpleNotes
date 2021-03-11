package com.example.simplenotes.presentation.main.alltasks

import android.animation.LayoutTransition
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.simplenotes.R
import com.example.simplenotes.databinding.RecyclerAllTasksItemBinding
import com.example.simplenotes.domain.entities.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*


class TaskAdapter(
    val context: Context,
    val updateStatusCallback: (status: Boolean, id: String) -> Unit
) : ListAdapter<Task, TaskAdapter.Holder>(DiffCallback) {

    class Holder(private val binding: RecyclerAllTasksItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        constructor(parent: ViewGroup) : this(
            RecyclerAllTasksItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )

        fun bind(
            context: Context,
            task: Task,
            updateStatusCallback: (status: Boolean, id: String) -> Unit
        ) {
            binding.apply {
                title.text = task.title

                priority.text = task.priority.toString()

                cardPriority.backgroundTintList = ColorStateList.valueOf(
                    when (task.priority) {
                        1 -> R.color.priority_1
                        2 -> R.color.priority_2
                        3 -> R.color.priority_3
                        4 -> R.color.priority_4
                        5 -> R.color.priority_5
                        else -> R.color.priority_3
                    }
                )

                task.deadline?.let {
                    deadline.text = convertLongToTime(it)
                    if (currentTimeToLong() > it && !task.status) {
                        indicator.visibility = View.VISIBLE
                        deadline.setTextColor(context.resources.getColor(R.color.red))
                    }
                }

                title.isChecked = task.status
                title.setOnClickListener {

                    if (!title.isChecked) {
                        title.isChecked = true
                        updateStatusCallback.invoke(true, task.id)
                    } else {
                        title.isChecked = false
                        updateStatusCallback.invoke(false, task.id)
                    }
                }
            }
        }

        private fun convertLongToTime(time: Long) =
            SimpleDateFormat("yyyy.MM.dd HH:mm").format(Date(time))

        private fun currentTimeToLong() = System.currentTimeMillis()

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
        holder.bind(context, getItem(position)) { status: Boolean, id: String ->
            updateStatusCallback.invoke(status, id)
        }
    }

}