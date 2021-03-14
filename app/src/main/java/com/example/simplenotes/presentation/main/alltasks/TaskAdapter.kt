package com.example.simplenotes.presentation.main.alltasks

import android.animation.LayoutTransition
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
    val updateStatusCallback: (status: Boolean, id: String) -> Unit,
    val itemClicked:(id: String) -> Unit
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
            updateStatusCallback: (status: Boolean, id: String) -> Unit,
            itemClicked:(id: String) -> Unit
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

                if(title.isChecked){
                    setThatTaskCompleted(context)
                } else {
                    mayBeNeedToShowOverdue(task, context)
                }

                title.setOnClickListener {

                    if (!title.isChecked) {
                        title.isChecked = true
                        updateStatusCallback.invoke(true, task.id)
                        setThatTaskCompleted(context)

                    } else {
                        title.isChecked = false
                        updateStatusCallback.invoke(false, task.id)
                        mayBeNeedToShowOverdue(task, context)
                    }
                }

                wholeCard.setOnClickListener {
                    itemClicked.invoke(task.id)
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
        holder.bind(context, getItem(position), { status: Boolean, id: String ->
            updateStatusCallback.invoke(status, id)
        }, {
            itemClicked.invoke(it)
        })
    }

    fun getId (position: Int) : String {
        val task = getItem(position)
        return task.id
    }

}