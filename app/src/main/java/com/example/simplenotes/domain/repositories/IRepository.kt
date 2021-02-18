package com.example.simplenotes.domain.repositories

import com.example.simplenotes.domain.entities.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface IRepository {

    interface FirestoreRepository{
        suspend fun getAllTasksByUser(userId: String): QuerySnapshot

        //Тип для параметра task нужно изменить
        suspend fun addNewTask(userId: String, task: Boolean = false): Boolean

        suspend fun getTasksByCategoryId(userId: String, category: String): QuerySnapshot
        suspend fun createCategory(userId: String, name: String): Boolean
        suspend fun getAllCategoriesByUser(userId: String): QuerySnapshot
    }

    interface AuthRepository{
        suspend fun signIn(email: String, password: String): AuthResult
        suspend fun signUp(email: String, password: String): AuthResult
    }
}