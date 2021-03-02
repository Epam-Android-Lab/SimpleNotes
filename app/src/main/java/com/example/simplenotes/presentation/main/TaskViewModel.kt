package com.example.simplenotes.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotes.data.repositories.FirestoreRepository
import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.usecases.AddNewTaskUseCase
import com.example.simplenotes.domain.usecases.GetTaskByIdUseCase
import com.example.simplenotes.domain.usecases.UpdateTaskUseCase
import com.example.simplenotes.presentation.Contract
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel(), Contract.ITaskViewModel {

    private val _task = MutableLiveData<Task>()
    override val task: LiveData<Task>
        get() = _task

    override fun addNewTask(newTask: Task) : String {
        var id = ""
        viewModelScope.launch {
            id = AddNewTaskUseCase(FirestoreRepository()).execute(newTask)
        }
        return id
    }

    override fun getTask(id: String) {
        viewModelScope.launch {
            GetTaskByIdUseCase(FirestoreRepository()).execute(id)?.let {
                _task.postValue(it)
            }
        }
    }

    override fun updateTask(id: String, updatedTask: Task) {
        viewModelScope.launch {
            UpdateTaskUseCase(FirestoreRepository()).execute(id, updatedTask)
        }
    }
}