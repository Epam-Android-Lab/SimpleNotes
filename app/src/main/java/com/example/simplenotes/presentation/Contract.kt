package com.example.simplenotes.presentation

import androidx.lifecycle.LiveData
import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.presentation.login.AuthViewModel

interface Contract {

    interface IViewModel {

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

    }
}