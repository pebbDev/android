package com.example.infinite_track.presentation.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.infinite_track.presentation.theme.Blue_500
import com.example.infinite_track.presentation.theme.Infinite_TrackTheme

@Composable
fun DualIconButton(
    modifier: Modifier = Modifier,
    leftIcon: ImageVector,
    rightIcon: ImageVector,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    leftContentDescription: String = "Left Action",
    rightContentDescription: String = "Right Action"
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Icon Button
        IconButton(
            onClick = onLeftClick,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Blue_500,
                    shape = RoundedCornerShape(size = 6.dp)
                )
                .background(
                    Blue_500.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(size = 6.dp)
                )
                .size(32.dp)
        ) {
            Icon(
                imageVector = leftIcon,
                contentDescription = leftContentDescription,
                tint = Blue_500,
                modifier = Modifier.size(24.dp)
            )
        }

        // Right Icon Button
        IconButton(
            onClick = onRightClick,
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Blue_500,
                    shape = RoundedCornerShape(size = 6.dp)
                )
                .background(
                    Blue_500.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(size = 6.dp)
                )
                .size(32.dp)
        ) {
            Icon(
                imageVector = rightIcon,
                contentDescription = rightContentDescription,
                tint = Blue_500,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DualIconButtonPreview() {
    Infinite_TrackTheme {
        DualIconButton(
            modifier = Modifier.padding(16.dp),
            leftIcon = Icons.Default.ArrowBack,
            rightIcon = Icons.Default.ArrowForward,
            onLeftClick = { /* Handle left action */ },
            onRightClick = { /* Handle right action */ },
            leftContentDescription = "Go Back",
            rightContentDescription = "Go Forward"
        )
    }
}
