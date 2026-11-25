package com.example.campusconnect.presentation.profile.setup

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.campusconnect.presentation.profile.components.ProfileInputField
import com.example.campusconnect.presentation.profile.viewmodel.ProfileSetupViewModel
import com.example.campusconnect.ui.theme.CampusColors


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ProfileSetupScreen(
    onProfileCreated: () -> Unit,
    viewModel: ProfileSetupViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isProfileCreated) {
        if (uiState.isProfileCreated) {
            onProfileCreated()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CampusColors.Background)
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 22.dp)
                .padding(top = 28.dp, bottom = 28.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            // Header
            Text(
                text = "Campus Connect",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = CampusColors.TextPrimary,
                fontWeight = FontWeight.Medium
            )

            // Introduction
            Column(
                verticalArrangement = Arrangement.spacedBy(7.dp)
            ) {
                Text(
                    text = "Complete your profile.",
                    style = MaterialTheme.typography.headlineMedium,
                    color = CampusColors.TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )

                Text(
                    text = "Tell us a little about yourself and your academic background.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = CampusColors.TextSecondary
                )
            }

            // Personal information
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                ProfileSectionTitle("Personal information")

                ProfileInputField(
                    label = "Full name",
                    value = uiState.name,
                    onValueChange = viewModel::onNameChange,
                    placeholder = "Enter your full name"
                )

                ProfileInputField(
                    label = "Bio (optional)",
                    value = uiState.bio,
                    onValueChange = viewModel::onBioChange,
                    placeholder = "Write something about yourself",
                    singleLine = false,
                    minLines = 3
                )
            }

            HorizontalDivider(
                color = CampusColors.FieldBorder
            )

            // Academic information
            Column(
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                ProfileSectionTitle("Academic information")

                ProfileInputField(
                    label = "Department",
                    value = uiState.departmentId,
                    onValueChange = viewModel::onDepartmentChange,
                    placeholder = "e.g. Computer Science"
                )

                ProfileInputField(
                    label = "Course",
                    value = uiState.courseId,
                    onValueChange = viewModel::onCourseChange,
                    placeholder = "e.g. MCA"
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProfileInputField(
                        label = "Admission year",
                        value = uiState.admissionYear,
                        onValueChange = viewModel::onAdmissionYearChange,
                        placeholder = "2025",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )

                    ProfileInputField(
                        label = "Current year",
                        value = uiState.currentYear,
                        onValueChange = viewModel::onCurrentYearChange,
                        placeholder = "1",
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                }
                // todo remove section field
//                ProfileInputField(
//                    label = "Section (optional)",
//                    value = uiState.section,
//                    onValueChange = viewModel::onSectionChange,
//                    placeholder = "e.g. A"
//                )
            }

            // Error message
            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = CampusColors.Error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Save button
            Button(
                onClick = viewModel::saveProfile,
                enabled = !uiState.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(13.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CampusColors.Primary,
                    contentColor = CampusColors.White,
                    disabledContainerColor = CampusColors.Primary.copy(
                        alpha = 0.6f
                    )
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(22.dp),
                        color = CampusColors.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Save profile",
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(Modifier.width(8.dp))

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null
                    )
                }
            }

            Text(
                text = "Your academic information helps connect you with your campus community.",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = CampusColors.TextSecondary
            )
        }
    }
}

@Composable
private fun ProfileSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleSmall,
        color = CampusColors.Primary,
        fontWeight = FontWeight.SemiBold
    )
}
