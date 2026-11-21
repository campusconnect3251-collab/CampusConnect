package com.example.campusconnect.presentation.auth.register

import androidx.compose.runtime.Composable
import com.example.campusconnect.presentation.auth.viewmodel.AuthViewModel
import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.campusconnect.presentation.auth.components.AuthGoogleButton
import com.example.campusconnect.presentation.auth.components.AuthHeader
import com.example.campusconnect.presentation.auth.components.AuthPrimaryButton
import com.example.campusconnect.presentation.auth.components.AuthTextField

@Composable
fun RegisterScreen(
    authViewModel: AuthViewModel,
    onNavigateToLogin: () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var confirmPassword by rememberSaveable { mutableStateOf("") }

    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var confirmPasswordVisible by rememberSaveable { mutableStateOf(false) }

    var validationError by remember { mutableStateOf<String?>(null) }

    var googleError by remember { mutableStateOf<String?>(null) }

    val errorMessage = validationError ?: googleError ?: uiState.errorMessage

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFFEEF2FF),
                        Color(0xFFF8FAFC),
                        Color.White
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AuthHeader(
                title = "Create your account",
                subtitle = "Join your campus community today."
            )

            Spacer(Modifier.height(28.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 460.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Text(
                        text = "Get started",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF0F172A)
                    )

                    AuthTextField(
                        label = "Email address",
                        value = email,
                        onValueChange = {
                            email = it
                            validationError = null
                            authViewModel.clearError()
                        },
                        placeholder = "Enter your email",
                        keyboardType = KeyboardType.Email
                    )

                    AuthTextField(
                        label = "Password",
                        value = password,
                        onValueChange = {
                            password = it
                            validationError = null
                            authViewModel.clearError()
                        },
                        placeholder = "Create a password",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onPasswordVisibilityChange = {
                            passwordVisible = !passwordVisible
                        }
                    )

                    AuthTextField(
                        label = "Confirm password",
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            validationError = null
                            authViewModel.clearError()
                        },
                        placeholder = "Re-enter your password",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        passwordVisible = confirmPasswordVisible,
                        onPasswordVisibilityChange = {
                            confirmPasswordVisible =
                                !confirmPasswordVisible
                        }
                    )

                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp
                        )
                    }

                    AuthPrimaryButton(
                        text = "Create Account",
                        isLoading = uiState.isRegisterLoading,
                        onClick = {
                            validationError = when {
                                email.isBlank() ->
                                    "Please enter your email."

                                !Patterns.EMAIL_ADDRESS.matcher(
                                    email.trim()
                                ).matches() ->
                                    "Please enter a valid email address."

                                password.isBlank() ->
                                    "Please enter a password."

                                password.length < 6 ->
                                    "Password must contain at least 6 characters."

                                confirmPassword.isBlank() ->
                                    "Please confirm your password."

                                password != confirmPassword ->
                                    "Passwords do not match."

                                else -> null
                            }

                            if (validationError == null) {
                                authViewModel.registerWithEmail(
                                    email = email.trim(),
                                    password = password
                                )
                            }
                        }
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFFE2E8F0)
                        )

                        Text(
                            text = "  OR  ",
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )

                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = Color(0xFFE2E8F0)
                        )
                    }

                    AuthGoogleButton(
                        onIdTokenReceived = { idToken ->
                            googleError = null
                            authViewModel.signInWithGoogle(idToken)
                        },
                        onError = { message ->
                            googleError = message
                        },
                        isFirebaseLoading = uiState.isGoogleLoading
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Already have an account?",
                    color = Color(0xFF64748B),
                    fontSize = 14.sp
                )

                TextButton(
                    onClick = onNavigateToLogin
                ) {
                    Text(
                        text = "Sign in",
                        color = Color(0xFF4F46E5),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(Modifier.height(12.dp))
        }
    }
}
