package com.example.campusconnect.presentation.profile.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.campusconnect.ui.theme.CampusColors

@Composable
fun ProfileInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = CampusColors.TextPrimary,
            fontWeight = FontWeight.SemiBold
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(
                    text = placeholder,
                    color = CampusColors.TextSecondary.copy(alpha = 0.7f)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(13.dp),
            singleLine = singleLine,
            minLines = minLines,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = CampusColors.Primary,
                unfocusedBorderColor = CampusColors.FieldBorder,
                focusedTextColor = CampusColors.TextPrimary,
                unfocusedTextColor = CampusColors.TextPrimary,
                cursorColor = CampusColors.Primary,
                focusedContainerColor = CampusColors.White,
                unfocusedContainerColor = CampusColors.White
            )
        )
    }
}