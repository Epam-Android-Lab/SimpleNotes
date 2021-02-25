package com.example.simplenotes.domain.usecases

import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.repositories.IRepository

class AddNewTaskUseCase(private val firestoreRepository: IRepository.FirestoreRepository) {
    suspend fun execute(task: Task) = firestoreRepository.addTask(task)
}