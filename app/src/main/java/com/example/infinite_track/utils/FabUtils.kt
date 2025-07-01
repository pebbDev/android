package com.example.infinite_track.utils

import androidx.compose.runtime.Composable
import com.example.infinite_track.R

data class FabAttributes(val icon: Int, val isVisible: Boolean)

@Composable
fun getFabAttributesByRole(userRole: String): FabAttributes {
    return when (userRole) {
        "Internship" -> FabAttributes(R.drawable.ic_intern_fab, true)
        "Management" -> FabAttributes(R.drawable.ic_management, true)
        else -> FabAttributes(0, false)
    }
}
