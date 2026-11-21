package com.example.campusconnect.data.datasource.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await


class FirebaseAuthDataSource @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {

    suspend fun registerWithEmail(
        email: String,
        password: String
    ): FirebaseUser {
        return firebaseAuth
            .createUserWithEmailAndPassword(email, password)
            .await()
            .user ?: throw IllegalStateException(
            "Firebase user was not created"
        )
    }

    suspend fun loginWithEmail(
        email: String,
        password: String
    ): FirebaseUser {
        return firebaseAuth
            .signInWithEmailAndPassword(email, password)
            .await()
            .user ?: throw IllegalStateException(
            "Firebase user was not found"
        )
    }

    suspend fun signInWithGoogle(
        idToken: String
    ): FirebaseUser {

        val credential = GoogleAuthProvider
            .getCredential(idToken, null)

        return firebaseAuth
            .signInWithCredential(credential)
            .await()
            .user ?: throw IllegalStateException(
            "Google authentication failed"
        )
    }

    fun signOut() {
        firebaseAuth.signOut()
    }

    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }

    fun observeAuthState(): Flow<FirebaseUser?> = callbackFlow {

        val listener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser)
        }

        firebaseAuth.addAuthStateListener(listener)

        awaitClose {
            firebaseAuth.removeAuthStateListener(listener)
        }
    }
}
