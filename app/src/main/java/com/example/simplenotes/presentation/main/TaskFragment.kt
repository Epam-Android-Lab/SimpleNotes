package com.example.simplenotes.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentTaskBinding
import com.example.simplenotes.domain.entities.Task
import java.util.*

class TaskFragment : Fragment(R.layout.fragment_task) {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!
    private val taskViewModel by viewModels<TaskViewModel>()

    private lateinit var deadlineDialog: TaskDeadlineFragment
    private lateinit var reminderDialog: TaskReminderFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddDeadline.setOnClickListener {
            val deadlineDialog = TaskDeadlineFragment()
            deadlineDialog.show(childFragmentManager, "Deadline" )
        }

        binding.btnAddReminder.setOnClickListener {
            reminderDialog = TaskReminderFragment()
            reminderDialog.show(childFragmentManager, "Reminder" )
        }

        binding.buttonSaveTask.setOnClickListener {

            val newTask = Task(
                    id = 0,                                                     //исправить на автоинкремент с сохранением последнего значения
                    title = binding.editTextTaskTitle.text.toString(),
                    description = binding.editTextTextTaskDesc.text.toString(),
                    deadline = deadlineDialog.deadlineTime,
                    notification = reminderDialog.reminderTime,
                    priority = binding.sliderPriority.value.toInt(),            //добавить значение слайдера
                    category = binding.spinnerCategories.toString(),
                    status = false,
                    timeLastEdit = Calendar.getInstance().timeInMillis
            )

            taskViewModel.addNewTask(newTask)
        }
    }
}