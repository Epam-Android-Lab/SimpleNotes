package com.example.simplenotes.presentation.main

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.presentation.login.AuthViewModel

interface Contract {

    interface IMainViewModel {
        val categoryState: LiveData<List<Category>>
        val latestTaskState: LiveData<List<Task>>
        fun addCategory(category: Category)
        fun getAllCategories()
        fun getLatestTasks()
        fun subscribeToFireBase()
    }

    interface IAllTasksViewModel {
        fun getData(categoryId: String)
        fun updateStatus(status: Boolean, id: String)
        fun getOptions(context: Context)
        fun setActiveSortOption(index: Int)

    }

    interface IAuthViewModel{
        val state: LiveData<AuthViewModel.AuthState>
        fun signIn(email: String, password: String)
        fun signUp(email: String, password: String)

    }

    interface ITaskViewModel{
        val task: LiveData<Task>
        fun addNewTask(newTask: Task) : String
        fun getTask(id: String)
        fun updateTask(id: String, updatedTask: Task)
        fun updateStatus(status: Boolean, id: String)
        fun getCategories()

    }
}