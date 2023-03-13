package com.cureius.pocket.feature_pot.presentation.pots

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cureius.pocket.R

@Preview
@Composable
fun PotsScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Your Pots",
                color = MaterialTheme.colors.onBackground,
                textAlign = TextAlign.Center,
                style = TextStyle(fontWeight = FontWeight.Bold),
                fontSize = 24.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(4.dp, 0.dp)
            )
            Card(
                elevation = 4.dp, shape = RoundedCornerShape(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.primary.copy(alpha = 0.4f))
                        .padding(8.dp)
                        .height(32.dp)
                        .width(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Edit")
                }
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            item {
                AddPotCard()
            }
            item { PotItem() }
        }
    }
}


@Preview
@Composable
fun AddPotCard() {
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
                    tint = MaterialTheme.colors.onSurface.copy(alpha = 0.2f),
                    modifier = Modifier.fillMaxSize(0.7f)
                )
            }
        }
    }
}


@Preview
@Composable
fun PotItem() {
    val pot = ImageVector.vectorResource(id = R.drawable.pot)
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
                .padding(16.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight(0.4f)
                            .fillMaxWidth(0.6f)
                            .background(
                                MaterialTheme.colors.primary.copy(alpha = 0.4f),
                                RoundedCornerShape(8.dp)
                            )
                    ) {

                    }
                    JarCanvas()

                }
                /*IconButton(onClick = {
                    }) {
                        Icon(
                            imageVector = pot,
                            contentDescription = "add account",
                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.9f),
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(0.3f)
                        )
                    }*/
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Text(text = "Content")
                }
            }


        }
    }
}

@Composable
fun WaterJarAnimation(percentageFull: Int) {
    val animatedWaterHeight = remember { androidx.compose.animation.core.Animatable(0f) }
    val jarColor = Color.Transparent
    val waterColor = MaterialTheme.colors.primary.copy(alpha = 0.7f)
    // Animate the water height
    LaunchedEffect(percentageFull) {
        animatedWaterHeight.animateTo(100 * (percentageFull / 100f))
    }
    Canvas(
        modifier = Modifier
            .fillMaxHeight(0.9f)
            .fillMaxWidth(0.3f)
            .padding(4.dp)
    ) {
        val waterHeight = size.height * (percentageFull / 100f)

        // Draw the jar
        drawRect(
            color = jarColor,
            size = Size(size.width * 0.8f, size.height * 0.8f),
            topLeft = Offset(size.width * 0.1f, size.height * 0.1f),
            style = Stroke(4.dp.toPx())
        )

        // Draw the static water
        drawRect(
            color = waterColor,
            size = Size(size.width * 0.8f, waterHeight),
            topLeft = Offset(size.width * 0.1f, size.height * 0.9f - waterHeight),
        )

        // Draw the animated water
        drawRect(
            color = waterColor,
            size = Size(size.width * 0.8f, animatedWaterHeight.value),
            topLeft = Offset(size.width * 0.1f, size.height * 0.9f - animatedWaterHeight.value),
        )
    }
}


@Preview
@Composable
fun waterJar() {
    WaterJarAnimation(20)
}


@Preview
@Composable
fun JarCanvas() {
    Canvas(
        modifier = Modifier
            .size(300.dp)
    ) {
        val width = size.width
        val height = size.height
        val color = Color.Black

        // draw jar body
        drawPath(
            path = Path().apply {
                moveTo(width * 0.33f, height * 0.1f)

                arcTo(
                    Rect(
                        left = width * 0.30f,
                        top = height * 0.05f,
                        right = width * 0.35f,
                        bottom = height * 0.1f
                    ), 90f, 180f, false
                )
                lineTo(width * 0.66f, height * 0.05f)
                arcTo(
                    Rect(
                        left = width * 0.65f,
                        top = height * 0.05f,
                        right = width * 0.70f,
                        bottom = height * 0.1f
                    ), -90f, 180f, false
                )

                arcTo(
                    Rect(
                        left = width * 0.65f,
                        top = height * 0.1f,
                        right = width * 0.75f,
                        bottom = height * 0.2f
                    ), -90f, 160f, false
                )

                arcTo(
                    Rect(
                        left = width * 0.65f,
                        top = height * 0.2f,
                        right = width * 0.80f,
                        bottom = height * 0.35f
                    ), -90f, 90f, false
                )

                lineTo(width * 0.80f, height * 0.92f)
                /*arcTo(
                    Rect(
                        left = width * 0.785f,
                        top = height * 0.28f,
                        right = width * 0.82f,
                        bottom = height * 0.92f
                    ), -90f, 180f, false
                )*/
                arcTo(
                    Rect(
                        left = width * 0.65f,
                        top = height * 0.85f,
                        right = width * 0.80f,
                        bottom = height * 0.99f
                    ), 0f, 90f, false
                )
                lineTo(width * 0.33f, height * 0.99f)
                arcTo(
                    Rect(
                        left = width * 0.20f,
                        top = height * 0.85f,
                        right = width * 0.35f,
                        bottom = height * 0.99f
                    ), 90f, 90f, false
                )
                lineTo(width * 0.2f, height * 0.3f)
                arcTo(
                    Rect(
                        left = width * 0.20f,
                        top = height * 0.2f,
                        right = width * 0.35f,
                        bottom = height * 0.35f
                    ), 180f, 90f, false
                )
                arcTo(
                    Rect(
                        left = width * 0.25f,
                        top = height * 0.10f,
                        right = width * 0.35f,
                        bottom = height * 0.2f
                    ), 90f, 180f, false
                )
                lineTo(width * 0.33f, height * 0.1f)
                close()
            },
            color = color,
            style = Stroke(width = 2.dp.toPx())
        )
        // draw jar lid
        drawPath(
            path = Path().apply {
                moveTo(width * 0.33f, height * 0.1f)
                arcTo(
                    Rect(
                        left = width * 0.30f,
                        top = height * 0.05f,
                        right = width * 0.35f,
                        bottom = height * 0.1f
                    ), 90f, 180f, false
                )
                lineTo(width * 0.66f, height * 0.05f)
                arcTo(
                    Rect(
                        left = width * 0.65f,
                        top = height * 0.05f,
                        right = width * 0.70f,
                        bottom = height * 0.1f
                    ), -90f, 180f, false
                )
            },
            color = color,
            style = Fill
        )
        // draw jar top
        drawPath(
            path = Path().apply {
                moveTo(width * 0.25f, height * 0.2f)
                moveTo(width * 0.70f, height * 0.2f)
                arcTo(
                    Rect(
                        left = width * 0.30f,
                        top = height * 0.10f,
                        right = width * 0.35f,
                        bottom = height * 0.2f
                    ), 90f, 90f, false
                )

            },
            color = color,
            style = Stroke(width = 2.dp.toPx())
        )
    }
}
//pot shape done
