package com.example.campusconnect.data.datasource.remote

import com.example.campusconnect.data.model.UserProfileDto
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import jakarta.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirebaseUserProfileDataSource @Inject constructor(
    private val firestore: FirebaseFirestore
) {

    private val usersCollection =
        firestore.collection("users")

    suspend fun createProfile(profile: UserProfileDto) {
        usersCollection
            .document(profile.uid)
            .set(profile)
            .await()
    }

    suspend fun getProfile(uid: String): UserProfileDto? {
        val snapshot = usersCollection
            .document(uid)
            .get()
            .await()

        return snapshot.toObject(UserProfileDto::class.java)
    }

    fun observeProfile(uid: String): Flow<UserProfileDto?> =
        callbackFlow {

            val listener = usersCollection
                .document(uid)
                .addSnapshotListener { snapshot, error ->

                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }

                    val profile = snapshot
                        ?.toObject(UserProfileDto::class.java)

                    trySend(profile)
                }

            awaitClose {
                listener.remove()
            }
        }

    suspend fun updateProfile(profile: UserProfileDto) {
        usersCollection
            .document(profile.uid)
            .set(profile, SetOptions.merge())
            .await()
    }
}