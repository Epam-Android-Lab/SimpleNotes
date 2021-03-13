package com.example.simplenotes.domain.usecases

import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.repositories.IRepository

class UpdateTaskUseCase (private val firestoreRepository: IRepository.FirestoreRepository) {
    suspend fun execute(id: String, updatedTask: Task) = firestoreRepository.updateTask(id, updatedTask)
}