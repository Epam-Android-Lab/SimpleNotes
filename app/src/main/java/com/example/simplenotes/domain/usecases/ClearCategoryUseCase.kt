package com.example.simplenotes.domain.usecases

import com.example.simplenotes.domain.repositories.IRepository

class ClearCategoryUseCase(private val repository: IRepository.FirestoreRepository) {
    suspend fun execute(categoryName: String) = repository.clearCategory(categoryName)
}