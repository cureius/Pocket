package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cureius.pocket.R

@Composable
fun AddAccountRequest(position: Int = 0, onClick: () -> Unit) {
    val account = ImageVector.vectorResource(id = R.drawable.accounts)

    val buttonShape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = 12.dp,
        bottomEnd = 12.dp
    )
    val topWrapperShape = RoundedCornerShape(
        topStart = 24.dp,
        topEnd = 24.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )
    val midWrapperShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )
    val endWrapperShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 24.dp,
        bottomEnd = 24.dp
    )
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .background(
                MaterialTheme.colors.primary.copy(alpha = 0.15f),
                when (position) {
                    -1 -> topWrapperShape
                    0 -> midWrapperShape
                    1 -> endWrapperShape
                    else -> midWrapperShape
                }
            )
            .border(
                2.dp,
                MaterialTheme.colors.primary.copy(alpha = 0.4f),
                when (position) {
                    -1 -> topWrapperShape
                    0 -> midWrapperShape
                    1 -> endWrapperShape
                    else -> midWrapperShape
                }
            )
            .padding(8.dp)
            .clickable {
                onClick()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = "Add Accounts",
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp
                )
                Text(
                    text = "Press To Add Account, Necessary To Track",
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 12.sp
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background.copy(alpha = 0.9f), buttonShape),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {
                    onClick()
                }) {
                    Icon(
                        imageVector = account,
                        contentDescription = "Add Accounts",
                        tint = MaterialTheme.colors.secondary
                    )
                }
            }
        }

    }
    Spacer(modifier = Modifier.height(8.dp))
}