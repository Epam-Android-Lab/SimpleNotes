package com.example.simplenotes.presentation.main

import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.simplenotes.R
import com.example.simplenotes.databinding.FragmentTaskBinding
import com.example.simplenotes.domain.entities.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class TaskFragment : Fragment(R.layout.fragment_task) {

    private var _binding: FragmentTaskBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSaveTask.setOnClickListener {

            val newTask = Task(
                id = 0,                                                     //исправить на автоинкремент с сохранением последнего значения
                title = binding.editTextTaskTitle.text.toString(),
                description = binding.editTextTextTaskDesc.text.toString(),
                deadline = null,                                            //необходимо значение даты
                notification = null,                                        //необходимо значение даты
                priority = binding.sliderPriority.value.toInt(),            //необходимо значение слайдера
                category = binding.spinnerCategories.toString(),
                status = false,
                timeLastEdit = Calendar.getInstance().timeInMillis
            )

            val db = Firebase.firestore

            Firebase.auth.uid?.let { it1 ->
                db.collection("users").document(it1).collection("notes")
                    .add(newTask)
                    .addOnSuccessListener { documentReference ->
                        Toast.makeText(context, "Task added with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error $e while adding document", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }
}