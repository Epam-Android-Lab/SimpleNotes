package com.example.simplenotes.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.domain.entities.Task
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

    interface IAuthViewModel{
        val state: LiveData<AuthViewModel.AuthState>
        fun signIn(email: String, password: String)
        fun signUp(email: String, password: String)

    }

    interface ITaskViewModel{
        val task: LiveData<com.example.simplenotes.domain.entities.Task>
        fun addNewTask(newTask: com.example.simplenotes.domain.entities.Task)

    }

}