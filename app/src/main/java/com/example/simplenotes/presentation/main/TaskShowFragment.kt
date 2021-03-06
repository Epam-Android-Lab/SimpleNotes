package com.example.simplenotes.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentTaskShowBinding

class TaskShowFragment : Fragment(R.layout.fragment_task_show) {

    private var _binding: FragmentTaskShowBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel by viewModels<TaskViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTaskShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskId = TaskShowFragmentArgs.fromBundle(requireArguments()).id

        taskViewModel.getTask(taskId)

        taskViewModel.task.observe(viewLifecycleOwner) { it ->
            binding.textTaskTitle.text = it.title
            binding.textTaskDesc.text = it.description
            binding.textOfDeadline.text = it.deadline?.let { android.text.format.DateFormat.format("dd-MM-yyyy HH:mm", it) }
            binding.textOfReminder.text = it.notification?.let { android.text.format.DateFormat.format("dd-MM-yyyy HH:mm", it) }
            binding.textOfPriority.text = it.priority.toString()
            binding.textOfCategory.text = it.category
        }

        binding.checkStatus.setOnClickListener {

            if (!binding.checkStatus.isChecked) {
                binding.checkStatus.isChecked = true
            } else {
                binding.checkStatus.isChecked = false
                //update status
            }
        }

        binding.buttonEditTask.setOnClickListener {
            val args = TaskEditFragmentArgs(id = taskId).toBundle()
            findNavController().navigate(R.id.action_taskShowFragment_to_taskEditFragment, args)
        }
    }
}