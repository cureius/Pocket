package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cureius.pocket.R

@Composable
fun ItemRow(items: List<String>) {
    val add = ImageVector.vectorResource(id = R.drawable.add)
    val buttonShape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = 12.dp,
        bottomEnd = 12.dp
    )
    LazyRow(
        modifier = Modifier
            .height(76.dp),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
        )
    ) {

        items(items) { item ->
            if (item == "add") {
                RoundIconButton(icon = add,
                    label = "",
                    modifier = Modifier
                        .background(
                            MaterialTheme.colors.surface, CircleShape
                        )
                        .height(48.dp)
                        .width(48.dp),
                    iconColor = MaterialTheme.colors.onSurface,
                    onClick = {

                    })
            } else {
                Box(
                    modifier = Modifier
                        .width(60.dp)
                        .height(60.dp)
                        .background(MaterialTheme.colors.surface, buttonShape)
                        .padding(8.dp)
                ) {
                    Text(
                        text = item.toString(),
                        color = MaterialTheme.colors.error,
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}
