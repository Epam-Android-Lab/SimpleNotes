package com.example.simplenotes.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentTaskBinding

class TaskCreatedFragment : Fragment(R.layout.fragment_task_created) {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel by viewModels<TaskViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskId = TaskCreatedFragmentArgs.fromBundle(requireArguments()).id
        val task = taskViewModel.getTask(taskId)

        binding.editTextTaskTitle.setText(task.title)
        binding.editTextTextTaskDesc.setText(task.description)
        binding.textOfDeadline.text = task.deadline?.let { android.text.format.DateFormat.format("dd-MM-yyyy HH:mm", it) }
        binding.textOfReminder.text = task.notification?.let { android.text.format.DateFormat.format("dd-MM-yyyy HH:mm", it) }
        //binding.sliderPriority.value = task.priority.toFloat()

        //проверить
        resources.getStringArray(R.array.categories).forEachIndexed { index, s ->
            if(s == task.category){
                binding.spinnerCategories.setSelection(index)
            }
        }
    }
}