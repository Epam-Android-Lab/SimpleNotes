package com.example.simplenotes.domain.usecases

import com.example.simplenotes.domain.repositories.IRepository

class GetTaskByIdUseCase(private val firestoreRepository: IRepository.FirestoreRepository) {
    suspend fun execute(id: String) = firestoreRepository.getTasksById(id)
}