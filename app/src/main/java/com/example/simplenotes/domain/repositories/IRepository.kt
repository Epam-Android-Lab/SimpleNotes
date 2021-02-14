package com.example.simplenotes.domain.repositories

import com.google.firebase.auth.AuthResult

interface IRepository {

    interface AuthRepository{
        suspend fun signIn(email: String, password: String): AuthResult
        suspend fun signUp(email: String, password: String): AuthResult
    }
}