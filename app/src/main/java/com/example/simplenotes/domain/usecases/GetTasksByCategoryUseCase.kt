package com.example.simplenotes.domain.usecases

import com.example.simplenotes.domain.repositories.IRepository

class GetTasksByCategoryUseCase(private val firestoreRepository: IRepository.FirestoreRepository) {
    suspend fun execute(category: String) = firestoreRepository.getTasksByCategoryId(category)
}