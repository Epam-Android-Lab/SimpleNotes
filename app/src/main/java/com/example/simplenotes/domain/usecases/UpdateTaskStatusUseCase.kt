package com.example.simplenotes.domain.usecases

import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.repositories.IRepository

class UpdateTaskStatusUseCase(private val firestoreRepository: IRepository.FirestoreRepository) {
    suspend fun execute(status: Boolean, id: String) = firestoreRepository.updateStatus(status, id)
}