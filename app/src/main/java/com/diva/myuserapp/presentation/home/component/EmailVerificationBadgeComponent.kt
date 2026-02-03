package com.diva.myuserapp.presentation.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun EmailVerificationBadgeOnlyComponent(isVerified: Boolean) {
    val colorScheme = MaterialTheme.colorScheme

    val containerColor =
        if (isVerified) colorScheme.tertiaryContainer else colorScheme.errorContainer
    val contentColor =
        if (isVerified) colorScheme.onTertiaryContainer else colorScheme.onErrorContainer

    Card(
        modifier = Modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(0.5.dp, contentColor.copy(alpha = 0.2f))
    ) {
        Icon(
            imageVector = if (isVerified) Icons.Default.CheckCircle else Icons.Default.Warning,
            contentDescription = null,
            modifier = Modifier
                .size(18.dp)
                .padding(4.dp),
            tint = contentColor
        )
    }
}

@Composable
fun EmailVerificationBadgeComponent(
    isVerified: Boolean,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    val containerColor =
        if (isVerified) colorScheme.tertiaryContainer else colorScheme.errorContainer
    val contentColor =
        if (isVerified) colorScheme.onTertiaryContainer else colorScheme.onErrorContainer

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        border = BorderStroke(0.5.dp, contentColor.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = if (isVerified) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = null,
                modifier = Modifier.size(14.dp),
                tint = contentColor
            )
            Text(
                text = if (isVerified) "Verified" else "Not Verified",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = contentColor
            )
        }
    }
}