package com.example.simplenotes.data.repositories

import com.example.simplenotes.domain.repositories.IRepository
import com.google.firebase.firestore.QuerySnapshot

class FirestoreRepository: IRepository.FirestoreRepository {
    override suspend fun getAllTasksByUser(userId: String): QuerySnapshot {
        TODO("Not yet implemented")
    }

    override suspend fun addNewTask(userId: String, task: Boolean): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getTasksByCategoryId(userId: String, category: String): QuerySnapshot {
        TODO("Not yet implemented")
    }

    override suspend fun createCategory(userId: String, name: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCategoriesByUser(userId: String): QuerySnapshot {
        TODO("Not yet implemented")
    }
}