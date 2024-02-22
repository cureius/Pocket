package com.cureius.pocket.feature_pot.presentation.pots.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cureius.pocket.R
import com.cureius.pocket.feature_pot.presentation.add_pot.AddPotEvent
import com.cureius.pocket.feature_pot.presentation.add_pot.AddPotViewModel

@Preview
@Composable
fun AddPotCard(viewModel: AddPotViewModel = hiltViewModel()) {
    val add = ImageVector.vectorResource(id = R.drawable.add)
    val paddingModifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .height(168.dp)
        .background(MaterialTheme.colors.surface.copy(0.4f), RoundedCornerShape(16.dp))
    Box(
        modifier = paddingModifier,
        contentAlignment = Alignment.Center,
    ) {
        IconButton(onClick = {
            viewModel.onEvent(AddPotEvent.ToggleAddAccountDialog)
        }, modifier = Modifier.fillMaxSize()) {
            Icon(
                imageVector = add,
                contentDescription = "add pot",
                tint = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                modifier = Modifier.fillMaxSize(0.7f)
            )
        }
    }
}
