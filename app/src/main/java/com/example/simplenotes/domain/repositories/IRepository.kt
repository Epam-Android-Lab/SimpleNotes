package com.example.simplenotes.domain.repositories

import com.example.simplenotes.domain.entities.Category
import com.example.simplenotes.domain.entities.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.QuerySnapshot

interface IRepository {

    interface FirestoreRepository{

        suspend fun getAllTasks(): QuerySnapshot?
        suspend fun getAllCategories(): QuerySnapshot?
        suspend fun addTask(task: Task): String
        suspend fun createCategory(category: Category): Boolean
        suspend fun getTasksByCategoryId(category: String): QuerySnapshot?

        suspend fun updateStatus(status: Boolean, id: String)

        suspend fun getTasksById(taskId: String): Task?
        suspend fun updateTask(id: String, updatedTask: Task)
        suspend fun deleteTask(id: String)

        suspend fun deleteCategory(categoryName: String)
        suspend fun clearCategory(categoryName: String)


    }

    interface AuthRepository{
        suspend fun signIn(email: String, password: String): AuthResult
        suspend fun signUp(email: String, password: String): AuthResult
    }
}