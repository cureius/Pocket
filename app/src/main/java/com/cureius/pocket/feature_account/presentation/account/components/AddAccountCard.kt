package com.cureius.pocket.feature_account.presentation.account.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import com.cureius.pocket.R

@Composable
fun AddAccountCard() {
    val add = ImageVector.vectorResource(id = R.drawable.add)
    val paddingModifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .height(160.dp)
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 4.dp,
        modifier = paddingModifier,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .fillMaxSize()
        ) {
            IconButton(onClick = {

            }, modifier = Modifier.fillMaxSize()) {
                Icon(
                    imageVector = add,
                    contentDescription = "add account",
                    tint = MaterialTheme.colors.onSurface,
                    modifier = Modifier.fillMaxSize(0.7f)
                )
            }
        }
    }
}