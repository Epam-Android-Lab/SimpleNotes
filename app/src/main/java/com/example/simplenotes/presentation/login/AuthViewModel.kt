package com.example.simplenotes.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplenotes.domain.usecases.SignInUserUseCase
import com.example.simplenotes.domain.usecases.SignUpUserUseCase
import com.example.simplenotes.presentation.main.Contract
import com.google.firebase.auth.FirebaseAuthException
import kotlinx.coroutines.launch

class AuthViewModel(
    private val signInUserUseCase: SignInUserUseCase,
    private val signUpUserUseCase: SignUpUserUseCase,
) : ViewModel(), Contract.IAuthViewModel {

    private val _state = MutableLiveData<AuthState>()
    override val state: LiveData<AuthState>
        get() = _state

    override fun signIn(email: String, password: String) {
        viewModelScope.launch {
            try {
                signInUserUseCase.execute(email, password).user?.let {
                    _state.postValue(AuthState.Authorized)
                } ?: run {
                    _state.postValue(AuthState.Failed)
                }
            } catch (e: FirebaseAuthException) {
                _state.postValue(AuthState.Failed)
            }
        }
    }

    override fun signUp(email: String, password: String) {
        viewModelScope.launch {
            try {
                signUpUserUseCase.execute(email, password).user?.let {
                    _state.postValue(AuthState.Authorized)
                } ?: run {
                    _state.postValue(AuthState.Failed)
                }
            } catch (e: FirebaseAuthException) {
                _state.postValue(AuthState.Failed)
            }
        }
    }

    enum class AuthState {
        Authorized, Failed, None
    }
}