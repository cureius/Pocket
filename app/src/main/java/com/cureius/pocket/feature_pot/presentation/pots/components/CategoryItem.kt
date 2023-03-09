package com.cureius.pocket.feature_pot.presentation.pots.components

import androidx.compose.ui.graphics.vector.ImageVector
import com.cureius.pocket.feature_dashboard.domain.PotType

data class CategoryItem(
    val icon: ImageVector? = null,
    val name: String? = null,
    val fill: Double? = null,
    val adder: Boolean? = false
//    val type: PotType?,
)