package com.example.simplenotes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.presentation.login.AuthViewModel
import com.google.android.gms.tasks.Task

interface Contract {

    interface IMainViewModel {
        val state: LiveData<MutableList<Category>>
        fun addCategory(category: Category)
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