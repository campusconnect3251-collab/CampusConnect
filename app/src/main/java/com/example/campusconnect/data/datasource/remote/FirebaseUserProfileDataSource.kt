package com.example.campusconnect.data.datasource.remote

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.campusconnect.data.mapper.toDomain
import com.example.campusconnect.data.model.UserPrivateDto
import com.example.campusconnect.data.model.UserProfileDto
import com.example.campusconnect.domain.model.UserProfile
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

    private val privateUsersCollection =
        firestore.collection("users_private")

    suspend fun createProfile(
        profile: UserProfileDto,
        privateProfile: UserPrivateDto
    ) {
        val batch = firestore.batch()

        batch.set(
            usersCollection.document(profile.uid),
            profile
        )

        batch.set(
            privateUsersCollection.document(profile.uid),
            privateProfile
        )

        batch.commit().await()
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

    suspend fun updateProfile(
        profile: UserProfileDto,
        privateProfile: UserPrivateDto
    ) {
        val batch = firestore.batch()

        batch.set(
            usersCollection.document(profile.uid),
            profile,
            SetOptions.merge()
        )

        batch.set(
            privateUsersCollection.document(profile.uid),
            privateProfile,
            SetOptions.merge()
        )

        batch.commit().await()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun observeUsers(
        currentUserId: String
    ): Flow<List<UserProfile>> = callbackFlow {

        val listener = usersCollection
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val users = snapshot?.documents?.mapNotNull { document ->

                    document.toObject(UserProfileDto::class.java)
                        ?.copy(uid = document.id)
                        ?.toDomain()

                } ?: emptyList()

                trySend(
                    users.filter { it.uid != currentUserId }
                )
            }

        awaitClose {
            listener.remove()
        }
    }
}