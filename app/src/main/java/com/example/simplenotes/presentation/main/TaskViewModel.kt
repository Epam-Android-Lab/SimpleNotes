package com.example.simplenotes.presentation.main

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.presentation.Contract
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel(), Contract.ITaskViewModel {

    override val task: MutableLiveData<Task> by lazy { MutableLiveData() }

    override fun addNewTask(newTask: Task) {

        viewModelScope.launch {

            val db = Firebase.firestore

            Firebase.auth.uid?.let { it1 ->
                db.collection("users").document(it1).collection("notes")
                        .add(newTask)
                        .addOnSuccessListener { documentReference ->
                            //Toast.makeText(context, "Task added with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { e ->
                            //Toast.makeText(context, "Error $e while adding document", Toast.LENGTH_SHORT).show()
                        }
            }
        }
    }
}