package com.example.campusconnect.presentation.auth.google

import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.NoCredentialException
import com.example.campusconnect.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential

class GoogleSignInClient(
    private val context: Context
) {
    private val credentialManager =
        CredentialManager.create(context)

    suspend fun getGoogleIdToken(): String {
        val response = credentialManager.getCredential(
            context = context,
            request = createRequest(
                filterAuthorizedAccounts = false
            )
        )

        val credential = response.credential

        if (
            credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            return GoogleIdTokenCredential
                .createFrom(credential.data)
                .idToken
        }

        throw IllegalStateException(
            "Google ID token was not returned."
        )
    }

    private fun createRequest(
        filterAuthorizedAccounts: Boolean
    ): GetCredentialRequest {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(filterAuthorizedAccounts)
            .setServerClientId(
                context.getString(R.string.default_web_client_id)
            )
            .build()

        return GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
    }
}