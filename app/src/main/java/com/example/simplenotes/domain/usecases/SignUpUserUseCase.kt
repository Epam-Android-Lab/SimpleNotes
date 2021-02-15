package com.example.simplenotes.domain.usecases

import com.example.simplenotes.domain.repositories.IRepository

class SignUpUserUseCase(private val repository: IRepository.AuthRepository) {
    suspend fun execute(email: String, password: String) = repository.signUp(email, password)
}