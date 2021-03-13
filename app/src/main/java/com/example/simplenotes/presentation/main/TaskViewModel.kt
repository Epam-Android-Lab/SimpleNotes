package com.example.simplenotes.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.usecases.*
import kotlinx.coroutines.launch

class TaskViewModel(
    private val addNewTaskUseCase: AddNewTaskUseCase,
    private val getTaskByIdUseCase: GetTaskByIdUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val updateTaskStatusUseCase: UpdateTaskStatusUseCase,
    private val getAllCategoriesByUser: GetAllCategoriesByUser,
) : ViewModel(), Contract.ITaskViewModel {

    private val _task = MutableLiveData<Task>()
    override val task: LiveData<Task>
        get() = _task

    private val _listOfCategory = MutableLiveData<List<String>>()
    val listOfCategory: LiveData<List<String>>
        get() = _listOfCategory

    override fun addNewTask(newTask: Task): String {
        var id = ""
        viewModelScope.launch {
            id = addNewTaskUseCase.execute(newTask)
        }
        return id
    }

    override fun getTask(id: String) {
        viewModelScope.launch {
            getTaskByIdUseCase.execute(id)?.let {
                _task.postValue(it)
            }
        }
    }

    override fun updateTask(id: String, updatedTask: Task) {
        viewModelScope.launch {
            updateTaskUseCase.execute(id, updatedTask)
        }
    }

    override fun updateStatus(status: Boolean, id: String) {
        viewModelScope.launch {
            updateTaskStatusUseCase.execute(status, id)
        }
    }

    override fun getCategories() {
        viewModelScope.launch {
            getAllCategoriesByUser.execute().let { snapshot ->
                val list: MutableList<String> = mutableListOf()
                snapshot?.forEach {
                    list.add(it.toObject(Category::class.java).name)
                }
                _listOfCategory.postValue(list)
            }
        }
    }
}