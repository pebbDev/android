package com.example.infinite_track.presentation.components.button.customfab

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.utils.getFabAttributesByRole

@Composable
fun CustomFAB(userRole: String, onFabClick: () -> Unit) {
    val fabAttributes = getFabAttributesByRole(userRole)

    if (fabAttributes.isVisible) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .offset(y = 120.dp)
        ) {
            PulsatingCirclesWithIcon()
            FloatingActionButton(
                onClick = onFabClick,
                containerColor = Blue_500,
                shape = CircleShape,
                modifier = Modifier.size(64.dp)
            ) {
                Icon(
                    painter = painterResource(id = fabAttributes.icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}
