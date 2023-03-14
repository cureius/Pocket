package com.cureius.pocket.feature_pot.presentation.pots

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.cureius.pocket.R
import com.cureius.pocket.feature_pot.presentation.add_pot.cmponents.AddPotDialog
import com.cureius.pocket.feature_pot.presentation.pots.components.Chart

@Composable
fun PotsScreen(navHostController: NavHostController, viewModel: PotsViewModel = hiltViewModel()) {
    val state = viewModel.state.value
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
                        .clickable {
                            navHostController.navigate("configure_pots")
                        },
                    contentAlignment = Alignment.Center
                ) {
                    val config = ImageVector.vectorResource(id = R.drawable.sliders)
                    Icon(
                        imageVector = config,
                        contentDescription = "config",
                        tint = MaterialTheme.colors.background,
                    )
                }
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(state) { pot ->
                PotItem()
            }
            item {
                AddPotCard(viewModel)
            }
        }
    }
    if (viewModel.isDialogShown) {
        AddPotDialog(onDismiss = {
            viewModel.onDismissDialog()
        }, onSubmit = {

        })
    }

}


@Preview
@Composable
fun AddPotCard(viewModel: PotsViewModel = hiltViewModel()) {
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
                viewModel.onAddClick()
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
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
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
                            val wallet = ImageVector.vectorResource(id = R.drawable.wallet)
                            Icon(
                                imageVector = wallet,
                                contentDescription = "add account",
                                tint = MaterialTheme.colors.surface,
                            )
                        }
                        Text(
                            text = "Monthly Wallet",
                            color = MaterialTheme.colors.onBackground,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(4.dp, 0.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(
                                MaterialTheme.colors.primaryVariant,
                                RoundedCornerShape(8.dp)
                            )
                    ) {
                        Row() {
                            Text(
                                text = "2k/10k",
                                color = MaterialTheme.colors.onPrimary,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontWeight = FontWeight.Normal),
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(4.dp)
                            )
                            Text(
                                text = " 20%",
                                color = MaterialTheme.colors.onSecondary,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .background(
                                        MaterialTheme.colors.secondary,
                                        RoundedCornerShape(4.dp)
                                    )
                            )
                        }

                    }
                }
                Box {
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
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight(0.4f)
                                    .fillMaxWidth(0.6f)
                                    .background(
                                        MaterialTheme.colors.primary.copy(alpha = 0.9f),
                                        RoundedCornerShape(8.dp)
                                    )
                            )
                            JarCanvas(MaterialTheme.colors.onSurface)
                        }

                        Box(
                            modifier = Modifier.background(Color.Yellow.copy(alpha = 0.0f))
                        ) {
                            Chart(
                                data = mapOf(
                                    Pair("mo", 0.5f),
                                    Pair("tu", 1f),
                                    Pair("we", 0.6f),
                                    Pair("th", 0.2f),
                                    Pair("fr", 0.3f),
                                    Pair("sa", 0.7f),
                                    Pair("su", 0.8f),
                                ),
                                maxValue = 100,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = (8).dp)
                            )
                        }
                    }
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
fun JarCanvas(color: Color = Color.Black) {
    Canvas(
        modifier = Modifier.size(300.dp),
    ) {
        val width = size.width
        val height = size.height

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

                lineTo(width * 0.80f, height * 0.92f)/*arcTo(
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
            }, color = color, style = Stroke(width = 2.dp.toPx())
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
            }, color = color, style = Fill
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

            }, color = color, style = Stroke(width = 2.dp.toPx())
        )
    }
}
//pot shape done
