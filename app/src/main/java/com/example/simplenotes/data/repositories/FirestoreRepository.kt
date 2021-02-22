package com.example.simplenotes.data.repositories

import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.repositories.IRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreRepository : IRepository.FirestoreRepository {

    companion object {
        const val USERS = "users"
        const val CATEGORIES = "categories"
    }

    private val db = Firebase.firestore

    override suspend fun createCategory(category: Category): Boolean {
        val doc = db.collection(USERS).document()
        category.id = doc.id

        return Firebase.auth.uid?.let { block ->
            db.collection(USERS).document(block).collection(CATEGORIES).document(category.id)
                    .set(category)
                    .isSuccessful
        } ?: false
    }

    override suspend fun getAllCategories(): QuerySnapshot {
        TODO("Not yet implemented")
    }

    override suspend fun getAllTasks(): QuerySnapshot {
        TODO("Not yet implemented")
    }

    override suspend fun addNewTask(task: Task): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getTasksByCategoryId(category: String): QuerySnapshot {
        TODO("Not yet implemented")
    }
}