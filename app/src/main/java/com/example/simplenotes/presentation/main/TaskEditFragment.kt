package com.example.simplenotes.presentation.main

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.format.DateFormat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.simplenotes.R
import com.example.simplenotes.data.repositories.FirestoreRepository
import com.example.simplenotes.databinding.FragmentTaskEditBinding
import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.usecases.GetAllCategoriesByUser
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_task_edit.*
import java.util.*
import kotlin.math.absoluteValue

class TaskEditFragment : Fragment(R.layout.fragment_task_edit) {

    private var _binding: FragmentTaskEditBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel: TaskViewModel by viewModel()

    private var deadlineTime: Long? = null
    private var reminderTime: Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTaskEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val outlinedTaskDesc = binding.outlinedTaskDesc.editText
        outlinedTaskDesc?.maxLines = 4
        outlinedTaskDesc?.setScroller(Scroller(context))
        outlinedTaskDesc?.isVerticalScrollBarEnabled = true
        //outlinedTaskDesc?.movementMethod = ScrollingMovementMethod()

        val toolbar = binding.topAppBar
        val appBarConfiguration = AppBarConfiguration(findNavController().graph)
        val navHostFragment = NavHostFragment.findNavController(this)
        NavigationUI.setupWithNavController(toolbar, navHostFragment,appBarConfiguration)
        setHasOptionsMenu(true)

        val taskId = TaskShowFragmentArgs.fromBundle(requireArguments()).id
        val deadline_notif_id = TaskShowFragmentArgs.fromBundle(requireArguments()).deadlineNotifId
        val reminder_notif_id = TaskShowFragmentArgs.fromBundle(requireArguments()).reminderNotifId

        taskViewModel.getTask(taskId)

        taskViewModel.task.observe(viewLifecycleOwner) { it ->
            binding.outlinedTaskTitle.editText?.setText(it.title)
            binding.outlinedTaskDesc.editText?.setText(it.description)
            binding.textOfDeadline.text = it.deadline?.let { DateFormat.format("dd-MM-yyyy HH:mm", it) }
            binding.textOfReminder.text = it.notification?.let { DateFormat.format("dd-MM-yyyy HH:mm", it) }
            binding.sliderPriority.value = it.priority.absoluteValue.toFloat()
            binding.actCategoties.setText(it.category.toString(),false)

            deadlineTime = it.deadline
            reminderTime = it.notification
        }

        taskViewModel.getCategories().also {
            taskViewModel.listOfCategory.observe(viewLifecycleOwner) {
                val adapter = ArrayAdapter(
                    requireContext(),
                    R.layout.list_category_item,
                    it
                )
                binding.actCategoties.setAdapter(adapter)
            }
        }

        binding.btnAddDeadline.setOnClickListener {
            setTime(binding.textOfDeadline, DEADLINE_ID)
        }

        binding.btnAddReminder.setOnClickListener {
            setTime(binding.textOfReminder, REMINDER_ID)
        }

        binding.buttonSaveTask.setOnClickListener {

            if(binding.outlinedTaskTitle.editText?.text.toString() == "") {
                Toast.makeText(context, "Заполните поле с заголовком задачи", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val updatedTask = Task(
                id = taskId,
                title = binding.outlinedTaskTitle.editText?.text.toString(),
                description = binding.outlinedTaskDesc.editText?.text.toString(),
                deadline = deadlineTime,
                notification = reminderTime,
                priority = binding.sliderPriority.value.toInt(),
                category = binding.actCategoties.text.toString(),
                status = false,
                timeLastEdit = Calendar.getInstance().timeInMillis
            )

            val args = TaskEditFragmentArgs(
                id = taskId,
                deadlineNotifId = deadline_notif_id,
                reminderNotifId = reminder_notif_id
            ).toBundle()

            taskViewModel.updateTask(taskId, updatedTask)

            deadlineTime?.let {
                setAlarm(
                    args,
                    DEADLINE_ID,
                    it,
                    "The task's deadline has expired!",
                    binding.outlinedTaskTitle.editText?.text.toString(),
                    deadline_notif_id
                )
            }

            reminderTime?.let {
                setAlarm(
                    args,
                    REMINDER_ID,
                    it,
                    "Reminder",
                    binding.outlinedTaskTitle.editText?.text.toString(),
                    reminder_notif_id
                )
            }

            Toast.makeText(context, "Задача изменена", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_taskEditFragment_to_taskShowFragment, args)
        }
    }

    private fun setTime(view: TextView, id: String) {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND,0)
            this.set(Calendar.MILLISECOND,0)
            activity?.let {
                DatePickerDialog (
                    it,
                    0,
                    { _, year, month, dayOfMonth ->
                        this.set(Calendar.YEAR, year)
                        this.set(Calendar.MONTH, month)
                        this.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                        TimePickerDialog(
                            it,
                            0,
                            { _, hour, min ->
                                this.set(Calendar.HOUR_OF_DAY, hour)
                                this.set(Calendar.MINUTE, min)

                                val dateFormatted = DateFormat.format("dd-MM-yyyy HH:mm", this)
                                view.text = dateFormatted
                                when (id) {
                                    DEADLINE_ID -> {
                                                deadlineTime = this.timeInMillis
                                                binding.textOfDeadline.visibility = View.VISIBLE
                                            }
                                    REMINDER_ID -> {
                                                reminderTime = this.timeInMillis
                                                binding.textOfReminder.visibility = View.VISIBLE
                                            }
                                }
                            },
                            this.get(Calendar.HOUR_OF_DAY),
                            this.get(Calendar.MINUTE),
                            true
                        ).show()
                    },
                    this.get(Calendar.YEAR),
                    this.get(Calendar.MONTH),
                    this.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

    private fun setAlarm(args: Bundle, type: String, time: Long, title: String, description: String, notificationCode: Int) {
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra(TYPE_NOTIFY, type)
        intent.putExtra(TITLE_NAME, title)
        intent.putExtra(DESC_NAME, description)
        intent.putExtra(TASK_ID, args)
        intent.putExtra(NOTIFICATION_ID, notificationCode)
        val pendingIntent = PendingIntent.getBroadcast(context, notificationCode, intent, PendingIntent.FLAG_CANCEL_CURRENT)
        val alarmManager : AlarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent)
    }

    companion object {
        private const val DEADLINE_ID = "DEADLINE_ID"
        private const val REMINDER_ID = "REMINDER_ID"
        private const val TITLE_NAME = "TITLE_NAME"
        private const val DESC_NAME = "DESC_NAME"
        private const val TASK_ID = "TASK_ID"
        private const val TYPE_NOTIFY = "TYPE_NOTIFY"
        private const val NOTIFICATION_ID = "NOTIFICATION_ID"
    }
}