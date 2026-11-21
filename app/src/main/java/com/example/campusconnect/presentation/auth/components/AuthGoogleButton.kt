package com.example.campusconnect.presentation.auth.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.campusconnect.presentation.auth.google.GoogleSignInClient
import kotlinx.coroutines.launch

@Composable
fun AuthGoogleButton(
    onIdTokenReceived: (String) -> Unit,
    onError: (String) -> Unit,
    isFirebaseLoading: Boolean = false
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val googleSignInClient = remember(context) {
        GoogleSignInClient(context)
    }

    var isCredentialLoading by remember {
        mutableStateOf(false)
    }

    val isLoading = isCredentialLoading || isFirebaseLoading

    OutlinedButton(
        onClick = {
            scope.launch {
                isCredentialLoading = true

                try {
                    val idToken =
                        googleSignInClient.getGoogleIdToken()

                    onIdTokenReceived(idToken)

                } catch (e: GetCredentialCancellationException) {
                    // The user dismissed the account picker.
                } catch (e: Exception) {
                    onError(
                        e.localizedMessage
                            ?: "Google Sign-In failed."
                    )
                } finally {
                    isCredentialLoading = false
                }
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(54.dp),
        enabled = !isLoading,
        shape = RoundedCornerShape(14.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                strokeWidth = 2.dp
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "G",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF4285F4)
                )

                Spacer(Modifier.width(12.dp))

                Text(
                    text = "Continue with Google",
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}