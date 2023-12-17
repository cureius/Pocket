package com.cureius.pocket.feature_pot.presentation.pots.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cureius.pocket.feature_pot.domain.util.IconDictionary
import com.cureius.pocket.feature_pot.domain.model.Pot
import java.util.Random
import kotlin.math.roundToInt

@Composable
fun PotItem(
    pot: Pot, data: Map<String, Float>? = mapOf(
        Pair("mo", 0.0f),
        Pair("tu", 0.0f),
        Pair("we", 0.0f),
        Pair("th", 0.0f),
        Pair("fr", 0.0f),
        Pair("sa", 0.0f),
        Pair("su", 0.0f),
    )
) {
    var filled = pot.filled;
    var mData: Map<String, Float>? = data
    val icon = IconDictionary.allIcons[pot.icon]
    val maxValue = (pot.capacity?.let { data?.values?.maxOrNull()?.times(it) })?.roundToInt()
    val paddingModifier = Modifier
        .padding(8.dp)
        .fillMaxWidth()
        .height(168.dp)
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
                .padding(8.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .padding(start = 8.dp, top = 8.dp, bottom = 4.dp)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colors.primary.copy(alpha = 0.4f),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(2.dp),
                    ) {
                        Box(
                            modifier = Modifier
                                .size(24.dp)
                                .background(
                                    MaterialTheme.colors.secondary, RoundedCornerShape(12.dp)
                                )
                                .padding(4.dp)
                        ) {
                            val iconVector = icon?.let { ImageVector.vectorResource(id = it) }
                            if (iconVector != null) {
                                Icon(
                                    imageVector = iconVector,
                                    contentDescription = "add account",
                                    tint = MaterialTheme.colors.surface,
                                )
                            }
                        }
                        pot.title?.let {
                            Text(
                                text = it,
                                color = MaterialTheme.colors.onBackground,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                fontSize = 16.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(4.dp, 0.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier.background(
                            MaterialTheme.colors.primaryVariant, RoundedCornerShape(8.dp)
                        )
                    ) {
                        Row {
                            if (pot.capacity != null) {
                                Text(
                                    text = "${(pot.capacity * filled!!).roundToInt()}/${pot.capacity.roundToInt()}",
                                    color = MaterialTheme.colors.onPrimary,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(fontWeight = FontWeight.Normal),
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.padding(4.dp)
                                )
                            }
                            if (filled != null) {
                                Text(
                                    text = " ${(filled!! * 100).roundToInt()}%",
                                    color = MaterialTheme.colors.onSecondary,
                                    textAlign = TextAlign.Center,
                                    style = TextStyle(fontWeight = FontWeight.Bold),
                                    fontSize = 12.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .background(
                                            MaterialTheme.colors.secondary, RoundedCornerShape(4.dp)
                                        )
                                )
                            }
                        }

                    }
                }


                Box(contentAlignment = Alignment.Center) {
                    if (pot.is_template == true) {
                        filled = kotlin.random.Random.nextFloat()
                        mData = mapOf(
                            Pair("mo", kotlin.random.Random.nextFloat()),
                            Pair("tu", kotlin.random.Random.nextFloat()),
                            Pair("we", kotlin.random.Random.nextFloat()),
                            Pair("th", kotlin.random.Random.nextFloat()),
                            Pair("fr", kotlin.random.Random.nextFloat()),
                            Pair("sa", kotlin.random.Random.nextFloat()),
                            Pair("su", kotlin.random.Random.nextFloat()),
                        )
                    }

                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .fillMaxHeight(),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            if (filled != null) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxHeight(filled!!)
                                        .fillMaxWidth(0.6f)
                                        .background(
                                            MaterialTheme.colors.primary.copy(alpha = 0.9f),
                                            RoundedCornerShape(8.dp)
                                        )
                                )
                            }

                            Jar(MaterialTheme.colors.onSurface)
                        }

                        Box(
                            modifier = Modifier.background(Color.Yellow.copy(alpha = 0.0f))
                        ) {
                            if (mData != null) {
                                Chart(
                                    data = mData!!,
                                    maxValue = maxValue?.toDouble(),
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .offset(y = (8).dp)
                                )
                            }
                        }
                    }
                    Box(
                        modifier = Modifier
                            .background(
                                color = if (pot.is_template == true && pot.weight == null) {
                                    MaterialTheme.colors.surface.copy(alpha = 0.95f)
                                } else if (pot.is_template == true && pot.weight != null) {
                                    MaterialTheme.colors.surface.copy(alpha = 0.90f)
                                } else {
                                    MaterialTheme.colors.surface.copy(alpha = 0.0f)
                                }, shape = RoundedCornerShape(24.dp)
                            )
                            .fillMaxSize(),
                    )
                    if (pot.is_template == true && pot.weight == null) {
                        Text(
                            text = "Set Weightage",
                            color = MaterialTheme.colors.onBackground,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            fontSize = 20.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(4.dp, 0.dp)
                        )
                    } else if (pot.is_template == true && pot.weight != null) {
                        Text(
                            text = "Not Initialized",
                            color = MaterialTheme.colors.onBackground,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            fontSize = 20.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier
                                .padding(4.dp, 0.dp)
                                .fillMaxSize()
                        )
                    } else {

                    }
                }
            }
        }
    }
}

