package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cureius.pocket.feature_dashboard.domain.PotType


@Composable
fun PotItem(
    icon: ImageVector,
    filled: Double = 0.0,
    name: String,
    potType: PotType,
) {
    val potShape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = 12.dp,
        bottomEnd = 12.dp
    )
    val fillShape = RoundedCornerShape(
        topStart = 8.dp,
        topEnd = 8.dp,
        bottomStart = 12.dp,
        bottomEnd = 12.dp
    )
    Column() {
        Box(contentAlignment = Alignment.Center) {
            Box(
                modifier = Modifier
                    .width(76.dp)
                    .height(100.dp)
                    .background(MaterialTheme.colors.surface, potShape),
                contentAlignment = Alignment.BottomCenter
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(
                            fraction = if (filled in 0.0..1.0) {
                                filled.toFloat()
                            } else {
                                1f
                            }
                        )
                        .background(
                            color =
                            when (potType) {
                                is PotType.Asset -> MaterialTheme.colors.secondary
                                is PotType.Liability -> MaterialTheme.colors.error
                            }, fillShape
                        ),
                    contentAlignment = Alignment.Center
                ) {

                }

            }
            Icon(
                imageVector = icon,
                contentDescription = name,
                modifier = Modifier.size(40.dp),
                tint = MaterialTheme.colors.background
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = name,
            color =
            when (potType) {
                is PotType.Asset -> MaterialTheme.colors.secondary
                is PotType.Liability -> MaterialTheme.colors.error
            },
            textAlign = TextAlign.Center,
            style = TextStyle(fontWeight = FontWeight.Bold),
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp, 0.dp)
        )
    }


    Spacer(modifier = Modifier.width(16.dp))

}