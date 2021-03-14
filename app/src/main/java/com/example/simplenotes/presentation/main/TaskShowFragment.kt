package com.example.simplenotes.presentation.main

import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentTaskShowBinding
import com.example.simplenotes.presentation.main.alltasks.AllTasksFragmentArgs
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_task_show.*
import java.text.SimpleDateFormat
import java.util.*

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

        val toolbar = binding.topAppBar
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment,appBarConfiguration)
        setHasOptionsMenu(true)

        val taskId = TaskShowFragmentArgs.fromBundle(requireArguments()).id
        val deadline_notif_id = TaskShowFragmentArgs.fromBundle(requireArguments()).deadlineNotifId
        val reminder_notif_id = TaskShowFragmentArgs.fromBundle(requireArguments()).reminderNotifId
        val id_category = TaskShowFragmentArgs.fromBundle(requireArguments()).idCategory
        val from_library = TaskShowFragmentArgs.fromBundle(requireArguments()).fromLibrary

        taskViewModel.getTask(taskId)

        taskViewModel.task.observe(viewLifecycleOwner) { task ->
            binding.textTaskTitle.text = task.title
            binding.textTaskDesc.text = task.description
            task.deadline?.let {
                textOfDeadline.text = convertLongToTime(it)
                if (currentTimeToLong() > it && !task.status) {
                    indicator.visibility = View.VISIBLE
                    context?.resources?.let { resources -> textOfDeadline.setTextColor(resources.getColor(R.color.red)) }
                }
            }

            textOfReminder.text = task.notification?.let { convertLongToTime(it) }
            textOfPriority.text = task.priority.toString()

            card_priority.backgroundTintList = ColorStateList.valueOf(
                when (task.priority) {
                    1 -> requireContext().resources.getColor(R.color.priority_1)
                    2 -> requireContext().resources.getColor(R.color.priority_2)
                    3 -> requireContext().resources.getColor(R.color.priority_3)
                    4 -> requireContext().resources.getColor(R.color.priority_4)
                    5 -> requireContext().resources.getColor(R.color.priority_5)
                    else -> requireContext().resources.getColor(R.color.priority_3)
                }
            )

            binding.textOfCategory.text = task.category
            binding.checkStatus.isChecked = task.status
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
                reminderNotifId = reminder_notif_id,

            ).toBundle()
            findNavController().navigate(R.id.action_taskShowFragment_to_taskEditFragment, args)
        }

        button_delete_task.setOnClickListener {
            taskViewModel.deleteTask(taskId)

            val args = AllTasksFragmentArgs(
                    categoryId = id_category,
                    fromLibrary = from_library
                    ).toBundle()
            findNavController().navigate(R.id.action_taskShowFragment_to_allTasksFragment2,args)
        }
    }

    private fun convertLongToTime(time: Long) =
        SimpleDateFormat("dd.MM.yyyy HH:mm").format(Date(time))

    private fun currentTimeToLong() = System.currentTimeMillis()
}