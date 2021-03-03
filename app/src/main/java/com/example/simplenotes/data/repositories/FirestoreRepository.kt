package com.example.simplenotes.data.repositories

import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.repositories.IRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirestoreRepository : IRepository.FirestoreRepository {

    companion object {
        const val COLLECTION_USERS = "users"
        const val COLLECTION_CATEGORIES = "categories"
        const val COLLECTION_NOTES = "notes"
    }

    private val db = Firebase.firestore
    private val userId = Firebase.auth.uid

    override suspend fun createCategory(category: Category): Boolean {
        val doc = db.collection(COLLECTION_USERS).document()
        category.id = doc.id

        return userId?.let { block ->
            db.collection(COLLECTION_USERS).document(block).collection(COLLECTION_CATEGORIES).document(category.id)
                    .set(category)
                    .isSuccessful
        } ?: false
    }

    override suspend fun getAllCategories() = userId?.let { userId ->
        db.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_CATEGORIES).get().await()
    }

    override suspend fun getAllTasks() = userId?.let { userId ->
        db.collection(COLLECTION_USERS).document(userId).collection(COLLECTION_NOTES).get().await()
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
                    .document(taskId)
                    .get().await().toObject(Task::class.java)
        }
    }

    override suspend fun updateTask(id: String, updatedTask: Task) {
        val taskRef = userId?.let {
            db.collection(COLLECTION_USERS)
                    .document(it)
                    .collection(COLLECTION_NOTES)
                    .document(id)
        }

        taskRef?.update("title", updatedTask.title)
        taskRef?.update("description", updatedTask.description)
        taskRef?.update("deadline", updatedTask.deadline)
        taskRef?.update("notification", updatedTask.notification)
        taskRef?.update("priority", updatedTask.priority)
        taskRef?.update("category", updatedTask.category)
        taskRef?.update("status", updatedTask.status)
        taskRef?.update("timeLastEdit", updatedTask.timeLastEdit)
    }
}