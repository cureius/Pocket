package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ActionItem(
    val icon: ImageVector? = null,
    val title: String? = null,
    val subTitle: String? = null,
    val borderColor: Color? = null,
    val backgroundColor: Color? = null,
    val position: Int? = 0,
    val condition: Boolean? = true,
    val onClick: () -> Unit
)