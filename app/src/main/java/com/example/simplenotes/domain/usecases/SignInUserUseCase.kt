package com.example.simplenotes.domain.usecases

import com.example.simplenotes.domain.repositories.IRepository

class SignInUserUseCase(private val repository: IRepository.AuthRepository) {
    suspend fun execute(email: String, password: String) = repository.signIn(email, password)
}