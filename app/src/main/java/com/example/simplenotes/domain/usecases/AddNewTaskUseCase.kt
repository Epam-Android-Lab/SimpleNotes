package com.example.simplenotes.domain.usecases

import com.example.simplenotes.domain.repositories.IRepository

class AddNewTaskUseCase(private val firestoreRepository: IRepository.FirestoreRepository) {
    suspend fun execute(userId: String, task: Boolean = false) = firestoreRepository.addNewTask(userId, task)
}