package com.example.simplenotes.domain.usecases

import com.example.simplenotes.data.repositories.FirestoreRepository
import com.example.simplenotes.domain.entities.Category

class CreateCategoryUseCase(private val firestoreRepository: FirestoreRepository) {
    suspend fun execute(category: Category) = firestoreRepository.createCategory(category)
}