package com.example.simplenotes.presentation.main

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import com.example.simplenotes.databinding.FragmentTaskDeadlineBinding
import java.util.*

class TaskDeadlineFragment : DialogFragment() {

    private var _binding: FragmentTaskDeadlineBinding? = null
    private val binding get() = _binding!!

    private lateinit var datePickerDialog: DatePickerDialog
    private lateinit var timePickerDialog: TimePickerDialog
    private lateinit var timePicker: TimePicker

    var deadlineTime: Long? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTaskDeadlineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

         timePicker = TimePicker(context)

        binding.btnDatePicker.setOnClickListener {
            handleDatePickButton()
        }

        binding.btnTimePicker.setOnClickListener {
            handleTimePickButton()
        }

        binding.btnSaveDeadline.setOnClickListener {
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(
                    datePickerDialog.datePicker.year,
                    datePickerDialog.datePicker.month,
                    datePickerDialog.datePicker.dayOfMonth,
                    timePicker.hour,
                    timePicker.minute
            )
            deadlineTime = calendar.timeInMillis

            dismiss()
        }

        binding.btnCancelDeadline.setOnClickListener {
            dismiss()
        }
    }

    private fun handleDatePickButton() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        context?.let {
            datePickerDialog = DatePickerDialog(requireContext(), DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DATE, dayOfMonth)
                val dateFormatted = android.text.format.DateFormat.format("EEEE, MMM d, yyyy", calendar)

                binding.textViewDate.text = dateFormatted

            }, year, month, day)
        }
        datePickerDialog.show()
    }

    private fun handleTimePickButton() {
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR)
        val minute = c.get(Calendar.MINUTE)

        timePickerDialog = TimePickerDialog(context, TimePickerDialog.OnTimeSetListener { view, hourOfDay, min ->

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR, hourOfDay)
            calendar.set(Calendar.MINUTE, min)
            val timeFormatted = android.text.format.DateFormat.format("HH:mm", calendar)

            timePicker.hour = hourOfDay
            timePicker.minute = min

            binding.textViewTime.text = timeFormatted

        }, hour, minute, true)

        timePickerDialog.show()

    }
}