package com.example.simplenotes.domain.usecases

import com.example.simplenotes.domain.repositories.IRepository

class DeleteTaskUseCase (private val repository: IRepository.FirestoreRepository) {
    suspend fun execute(id: String) = repository.deleteTask(id)
}