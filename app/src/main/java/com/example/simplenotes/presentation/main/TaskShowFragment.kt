package com.example.simplenotes.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentTaskShowBinding

class TaskShowFragment : Fragment(R.layout.fragment_task_show) {

    private var _binding: FragmentTaskShowBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel: TaskViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTaskShowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val taskId = TaskShowFragmentArgs.fromBundle(requireArguments()).id
        val deadline_notif_id = TaskShowFragmentArgs.fromBundle(requireArguments()).deadlineNotifId
        val reminder_notif_id = TaskShowFragmentArgs.fromBundle(requireArguments()).reminderNotifId

        taskViewModel.getTask(taskId)

        taskViewModel.task.observe(viewLifecycleOwner) { it ->
            binding.textTaskTitle.text = it.title
            binding.textTaskDesc.text = it.description
            binding.textOfDeadline.text = it.deadline?.let { android.text.format.DateFormat.format("dd-MM-yyyy HH:mm", it) }
            binding.textOfReminder.text = it.notification?.let { android.text.format.DateFormat.format("dd-MM-yyyy HH:mm", it) }
            binding.textOfPriority.text = it.priority.toString()
            binding.textOfCategory.text = it.category
            binding.checkStatus.isChecked = it.status
            if(it.category == "Выполнено") binding.checkStatus.isChecked = true
        }

        binding.checkStatus.setOnClickListener {
            if (!binding.checkStatus.isChecked) {
                binding.checkStatus.isChecked = true
                taskViewModel.updateStatus(true, taskId)
            } else {
                binding.checkStatus.isChecked = false
                taskViewModel.updateStatus(false, taskId)
            }
        }

        binding.buttonEditTask.setOnClickListener {
            val args = TaskEditFragmentArgs(
                id = taskId,
                deadlineNotifId = deadline_notif_id,
                reminderNotifId = reminder_notif_id
            ).toBundle()
            findNavController().navigate(R.id.action_taskShowFragment_to_taskEditFragment, args)
        }
    }
}