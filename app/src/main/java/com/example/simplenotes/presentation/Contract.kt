package com.example.simplenotes.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.simplenotes.presentation.login.AuthViewModel

interface Contract {

    interface IViewModel {

    }

    interface IAuthViewModel{
        val state: LiveData<AuthViewModel.AuthState>
        fun signIn(email: String, password: String)
        fun signUp(email: String, password: String)

    }
}