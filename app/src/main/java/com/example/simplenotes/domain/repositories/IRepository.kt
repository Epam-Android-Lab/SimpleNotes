package com.example.simplenotes.domain.repositories

import com.example.simplenotes.domain.entities.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface IRepository {

    interface FirestoreRepository{

        suspend fun getAllTasks(): QuerySnapshot
        suspend fun getAllCategories(): QuerySnapshot
        suspend fun addNewTask(task: Task): Boolean
        suspend fun createCategory(name: String): Boolean
        suspend fun getTasksByCategoryId(category: String): QuerySnapshot

    }

    interface AuthRepository{
        suspend fun signIn(email: String, password: String): AuthResult
        suspend fun signUp(email: String, password: String): AuthResult
    }
}