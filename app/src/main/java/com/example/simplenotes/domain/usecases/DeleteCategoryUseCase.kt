package com.example.simplenotes.domain.usecases

import com.example.simplenotes.domain.repositories.IRepository

class DeleteCategoryUseCase(private val repository: IRepository.FirestoreRepository) {
    suspend fun execute(categoryName: String) = repository.deleteCategory(categoryName)
}