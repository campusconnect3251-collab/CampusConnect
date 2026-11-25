package com.example.campusconnect.presentation.auth.login

import android.util.Patterns
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import com.example.campusconnect.presentation.auth.viewmodel.AuthViewModel
import com.example.campusconnect.ui.theme.CampusColors

@Composable
fun LoginScreen(
    authViewModel: AuthViewModel,
    onNavigateToRegister: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    val uiState by authViewModel.uiState.collectAsStateWithLifecycle()

    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var validationError by remember { mutableStateOf<String?>(null) }
    var googleError by remember { mutableStateOf<String?>(null) }

    val errorMessage = validationError ?: googleError ?: uiState.errorMessage

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        CampusColors.Background,
                        CampusColors.Background,
                        CampusColors.White
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

            // Branding and heading
            AuthHeader(
                title = "Welcome back!",
                subtitle = "Sign in to continue to your account."
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Login card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 460.dp),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = CampusColors.White
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
                        text = "Sign in",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = CampusColors.TextPrimary
                    )

                    // Email
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

                    // Password
                    AuthTextField(
                        label = "Password",
                        value = password,
                        onValueChange = {
                            password = it
                            validationError = null
                            authViewModel.clearError()
                        },
                        placeholder = "Enter your password",
                        keyboardType = KeyboardType.Password,
                        isPassword = true,
                        passwordVisible = passwordVisible,
                        onPasswordVisibilityChange = {
                            passwordVisible = !passwordVisible
                        }
                    )

                    // Forgot password
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        TextButton(
                            onClick = onForgotPasswordClick
                        ) {
                            Text(
                                text = "Forgot password?",
                                color = CampusColors.Primary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Error message
                    if (errorMessage != null) {
                        Text(
                            text = errorMessage,
                            color = CampusColors.Error,
                            fontSize = 13.sp
                        )
                    }

                    // Login button
                    AuthPrimaryButton(
                        text = "Sign In",
                        isLoading = uiState.isLoginLoading,
                        onClick = {
                            validationError = when {
                                email.isBlank() ->
                                    "Please enter your email."

                                !Patterns.EMAIL_ADDRESS.matcher(
                                    email.trim()
                                ).matches() ->
                                    "Please enter a valid email address."

                                password.isBlank() ->
                                    "Please enter your password."

                                else -> null
                            }

                            if (validationError == null) {
                                authViewModel.loginWithEmail(
                                    email = email.trim(),
                                    password = password
                                )
                            }
                        }
                    )

                    // OR divider
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = CampusColors.FieldBorder
                        )

                        Text(
                            text = "  OR  ",
                            color = CampusColors.TextSecondary,
                            fontSize = 12.sp
                        )

                        HorizontalDivider(
                            modifier = Modifier.weight(1f),
                            color = CampusColors.FieldBorder
                        )
                    }

                    // Google Sign-In
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

            Spacer(modifier = Modifier.height(24.dp))

            // Register navigation
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account?",
                    color = Color(0xFF64748B),
                    fontSize = 14.sp
                )

                TextButton(
                    onClick = onNavigateToRegister
                ) {
                    Text(
                        text = "Create account",
                        color = CampusColors.Primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun LoginScreenPreview(){
//    LoginScreen(
//        onGoogleSignInClick = {},
//        onForgotPasswordClick = {},
//        onNavigateToRegister = {}
//    )
//}