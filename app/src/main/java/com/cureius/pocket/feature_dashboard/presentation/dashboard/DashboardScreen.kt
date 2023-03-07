package com.cureius.pocket.feature_dashboard.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cureius.pocket.feature_dashboard.presentation.dashboard.components.DashBoardHeader

@Preview
@Composable
fun DashboardScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .height(320.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            DashBoardHeader()
            val shape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp,
                bottomStart = 24.dp,
                bottomEnd = 24.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(16.dp, 0.dp, 16.dp, 0.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colors.primaryVariant.copy(alpha = 0.5f), shape)
                        .fillMaxWidth()
                        .height(140.dp)
                ) {

                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            val wrapperShape = RoundedCornerShape(
                topStart = 24.dp,
                topEnd = 24.dp,
                bottomStart = 0.dp,
                bottomEnd = 0.dp
            )
            Spacer(modifier = Modifier.height(28.dp))
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colors.background, wrapperShape)
                    .fillMaxSize()
            ) {

            }
        }
    }
}


@Composable
fun MyScrollableList(components: List<Unit>) {
    LazyColumn {
        components.forEach { _ ->
            it()
        }
    }
}

fun it() {
    TODO("Not yet implemented")
}
