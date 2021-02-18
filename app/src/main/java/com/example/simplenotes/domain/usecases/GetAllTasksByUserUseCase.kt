package com.example.simplenotes.domain.usecases

import com.example.simplenotes.domain.repositories.IRepository

class GetAllTasksByUserUseCase(private val firestoreRepository: IRepository.FirestoreRepository) {
    suspend fun execute(userId: String) = firestoreRepository.getAllTasks()
}