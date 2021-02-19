package com.example.simplenotes.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotes.data.repositories.FirestoreRepository
import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.usecases.AddNewTaskUseCase
import com.example.simplenotes.presentation.Contract
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel(), Contract.ITaskViewModel {

    override val task: MutableLiveData<Task> by lazy { MutableLiveData() }

    override fun addNewTask(newTask: Task) {
        viewModelScope.launch {
        AddNewTaskUseCase(FirestoreRepository()).execute(newTask)
        }
    }
}