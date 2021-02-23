package com.example.simplenotes.data.repositories

import com.example.simplenotes.domain.entities.Task
import com.example.simplenotes.domain.repositories.IRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirestoreRepository: IRepository.FirestoreRepository {

    private val db = Firebase.firestore

    companion object {
        const val LIST_OF_USERS = "users"
        const val LIST_OF_NOTES = "notes"
    }


    override suspend fun getAllTasks(): QuerySnapshot {
        TODO("Not yet implemented")
    }

    override suspend fun getAllCategories(): QuerySnapshot {
        TODO("Not yet implemented")
    }

    override suspend fun addNewTask(task: Task): Boolean {
        val doc = db.collection(LIST_OF_USERS).document()
        task.id = doc.id

        return Firebase.auth.uid?.let { it1 ->
            db.collection(LIST_OF_USERS).document(it1).collection(LIST_OF_NOTES).document(task.id)
                    .set(task)
                    .isSuccessful
        } ?: false
    }

    override suspend fun getTasksByCategoryId(category: String): QuerySnapshot {
        TODO("Not yet implemented")
    }

    override suspend fun createCategory(name: String): Boolean {
        TODO("Not yet implemented")
    }
}