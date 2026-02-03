package com.diva.myuserapp.presentation.auth.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ValidationSupportingText(
    validations: List<FieldValidation>,
    modifier: Modifier = Modifier
) {
    if (validations.isEmpty()) return

    Column(modifier = modifier) {
        validations.forEach { validation ->
            val icon = when (validation.state) {
                ValidationState.NONE -> Icons.AutoMirrored.Default.KeyboardArrowRight
                ValidationState.VALID -> Icons.Default.Check
                ValidationState.INVALID -> Icons.Default.Close
            }

            val color = when (validation.state) {
                ValidationState.NONE -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                ValidationState.VALID -> MaterialTheme.colorScheme.primary
                ValidationState.INVALID -> MaterialTheme.colorScheme.error
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = validation.message,
                    color = color,
                    maxLines = 1,
                    fontSize = 12.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}