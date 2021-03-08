package com.example.simplenotes.presentation.main

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentTaskBinding
import com.example.simplenotes.domain.entities.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class TaskFragment : Fragment(R.layout.fragment_task) {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    private val taskViewModel: TaskViewModel by viewModel()

    private var deadlineTime: Long? = null
    private var reminderTime: Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddDeadline.setOnClickListener {
            setTime(binding.textOfDeadline, DEADLINE_ID)
        }

        binding.btnAddReminder.setOnClickListener {
            setTime(binding.textOfReminder, REMINDER_ID)
        }

        binding.buttonSaveTask.setOnClickListener {
            val newTask = Task(
                    id = "",
                    title = binding.editTextTaskTitle.text.toString(),
                    description = binding.editTextTextTaskDesc.text.toString(),
                    deadline = deadlineTime,
                    notification = reminderTime,
                    priority = binding.sliderPriority.value.toInt(),
                    category = binding.spinnerCategories.selectedItem.toString(),
                    status = false,
                    timeLastEdit = Calendar.getInstance().timeInMillis
            )

            val taskId = Firebase.auth.uid?.run {
                taskViewModel.addNewTask(newTask)
            }

            val deadline_notif_id = taskId.hashCode()
            val reminder_notif_id = taskId.hashCode() + 1

            val args = TaskShowFragmentArgs(
                id = taskId.toString(),
                deadlineNotifId = deadline_notif_id,
                reminderNotifId = reminder_notif_id
            ).toBundle()

            deadlineTime?.let {
                setAlarm(
                        args,
                        DEADLINE_ID,
                        it,
                        "The task's deadline has expired!",
                        binding.editTextTaskTitle.text.toString(),
                        deadline_notif_id
                )
            }

            reminderTime?.let {
                setAlarm(
                        args,
                        REMINDER_ID,
                        it,
                        "Reminder",
                        binding.editTextTaskTitle.text.toString(),
                        reminder_notif_id
                )
            }

            Toast.makeText(context, "Задача создана", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_taskFragment_to_mainScreenFragment)
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

                                        val dateFormatted = android.text.format.DateFormat.format("dd-MM-yyyy HH:mm", this)
                                        view.text = dateFormatted
                                        when (id) {
                                            DEADLINE_ID -> deadlineTime = this.timeInMillis
                                            REMINDER_ID -> reminderTime = this.timeInMillis
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
        val pendingIntent = PendingIntent.getBroadcast(context, notificationCode, intent, 0)
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