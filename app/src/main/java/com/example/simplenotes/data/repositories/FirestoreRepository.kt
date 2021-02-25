package com.example.simplenotes.data.repositories

import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.repositories.IRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirestoreRepository : IRepository.FirestoreRepository {

    private val db = Firebase.firestore
    private val userId = Firebase.auth.uid

    companion object {
        const val COLLECTION_USERS = "users"
        const val COLLECTION_NOTES = "notes"
        const val COLLECTION_CATEGORIES = "categories"
    }

    override suspend fun getAllTasks() = userId?.let { userId ->
        db.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_NOTES).get().await()
    }

    override suspend fun getAllCategories() = userId?.let { userId ->
        db.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_CATEGORIES).get().await()
    }

    override suspend fun addTask(task: Task): String {
        val doc = db.collection(COLLECTION_USERS).document()
        task.id = doc.id

        userId?.let { it1 ->
            db.collection(COLLECTION_USERS).document(it1).collection(COLLECTION_NOTES).document(task.id)
                .set(task)
        }
        return task.id
    }

    override suspend fun getTasksByCategoryId(category: String): QuerySnapshot? {
        return userId?.let { userId ->
            db.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_NOTES).whereEqualTo("category", category).get().await()
        }
    }

    override suspend fun getTasksById(taskId: String): Task? {

        return userId?.let { userId ->
            db.collection(COLLECTION_USERS)
                .document(userId)
                .collection(COLLECTION_NOTES)
                //.whereEqualTo("id",taskId)
                .document()
                .get().await().toObject(Task::class.java)
        }
    }

    override suspend fun createCategory(name: String): Boolean {
        TODO("Not yet implemented")
    }
}