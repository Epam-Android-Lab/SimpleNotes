package com.example.simplenotes.domain.usecases

import com.example.simplenotes.domain.repositories.IRepository

class CreateCategoryUseCase(private val firestoreRepository: IRepository.FirestoreRepository) {
    suspend fun execute(userId: String, name: String) = firestoreRepository.createCategory(name)
}