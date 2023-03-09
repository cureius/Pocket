package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cureius.pocket.R
import com.cureius.pocket.feature_pot.presentation.pots.components.CategoryItem

@Composable
fun ItemRow(items: List<CategoryItem>, modifier: Modifier = Modifier) {
    val add = ImageVector.vectorResource(id = R.drawable.add)
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
    LazyRow(
        modifier = modifier
            .height(80.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items) {item ->
            Spacer(modifier = Modifier.width(6.dp))
            if (item.adder == true) {
                RoundIconButton(icon = add,
                    label = "",
                    modifier = Modifier
                        .background(
                            MaterialTheme.colors.surface, CircleShape
                        )
                        .height(56.dp)
                        .width(56.dp),
                    iconColor = MaterialTheme.colors.onSurface,
                    onClick = {

                    })
            } else {
                Column(modifier = Modifier.width(60.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(60.dp)
                                .background(MaterialTheme.colors.surface, potShape),
                            contentAlignment = Alignment.BottomCenter
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(
                                        fraction = if (item.fill!! in 0.0..1.0) {
                                            item.fill.toFloat()
                                        } else {
                                            1f
                                        }
                                    )
                                    .background(
                                        color = MaterialTheme.colors.primary,
                                        /*when (potType) {
                                            is PotType.Asset -> MaterialTheme.colors.secondary
                                            is PotType.Liability -> MaterialTheme.colors.error
                                        }, */
                                        fillShape
                                    ),
                            )
                        }
                        Icon(
                            imageVector = item.icon!!,
                            contentDescription = item.name,
                            modifier = Modifier
                                .size(20.dp)
                                .offset(y = (16).dp),
                            tint = MaterialTheme.colors.background,
                        )
                    }
                    Text(
                        text = item.name!!,
                        color = MaterialTheme.colors.onSurface,
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontWeight = FontWeight.Normal),
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(0.dp, 0.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(6.dp))
        }
    }
}
