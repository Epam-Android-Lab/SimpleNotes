package com.example.simplenotes.data.repositories

import com.example.simplenotes.domain.repositories.IRepository
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class RemoteRepository: IRepository.AuthRepository {
    private lateinit var auth: FirebaseAuth

    override suspend fun signIn(email: String, password: String): AuthResult {
        auth = Firebase.auth
        return auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun signUp(email: String, password: String): AuthResult {
        auth = Firebase.auth
        return auth.createUserWithEmailAndPassword(email, password).await()
    }
}