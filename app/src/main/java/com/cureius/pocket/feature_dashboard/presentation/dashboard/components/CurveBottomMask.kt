package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cureius.pocket.feature_pot.presentation.pots.components.LeftConcaveMask
import com.cureius.pocket.feature_pot.presentation.pots.components.RightConcaveMask

@Preview
@Composable
fun CurveBottomMask(cornerColor: Color = Color.Black) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            LeftConcaveMask(modifier = Modifier
                .width(40.dp)
                .height(40.dp), cornerColor)
            RightConcaveMask(modifier = Modifier
                .width(40.dp)
                .height(40.dp), cornerColor)
        }
    }
}
