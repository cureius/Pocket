package com.cureius.pocket.feature_dashboard.presentation.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.navigation.NavHostController
import com.cureius.pocket.R
import com.cureius.pocket.feature_dashboard.presentation.dashboard.components.*
import com.cureius.pocket.util.ScreenDimensions

@Composable
fun DashboardScreen(navHostController: NavHostController) {
    val buttonShape = RoundedCornerShape(
        topStart = 12.dp,
        topEnd = 12.dp,
        bottomStart = 12.dp,
        bottomEnd = 12.dp
    )
    val gridItems = listOf<String>(
        "Food",
        "Entertainment",
        "Travel",
        "Home",
        "Subscription",
        "Cash",
        "Self Care",
        "add"
    )
    val potItems = listOf<String>(
        "Savings",
        "Wallet",
        "EMI",
        "Investment",
    )
    val size = ScreenDimensions()
    val screenWeight = size.width()
    val add = ImageVector.vectorResource(id = R.drawable.add)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colors.surface)
                .height(280.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            DashBoardHeader(navHostController)
            InfoSection()
            CurveBottomMask()
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .offset(y = (-30).dp),
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp)
        ) {
            item {
                IncomeCreditPrompt()
            }
            item {
                Text(
                    text = "Your Pots",
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp, 0.dp)
                )
            }
            item {
                LazyRow(
                    modifier = Modifier
                        .height(128.dp),
                    contentPadding = PaddingValues(
                        start = 0.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 16.dp
                    )
                ) {
                    item {
                        Column(
                            verticalArrangement = Arrangement.SpaceEvenly,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Spacer(modifier = Modifier.height(8.dp))
                            RoundIconButton(icon = add,
                                label = "",
                                modifier = Modifier
                                    .background(
                                        MaterialTheme.colors.onBackground, CircleShape
                                    )
                                    .height(44.dp)
                                    .width(44.dp),
                                iconColor = MaterialTheme.colors.background,
                                onClick = {

                                })
                            Text(
                                text = "Create new",
                                color = MaterialTheme.colors.onBackground,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }

                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    items(potItems){ item ->
                        Box(
                            modifier = Modifier
                                .width(76.dp)
                                .height(124.dp)
                                .background(MaterialTheme.colors.surface, buttonShape)
                                .padding(8.dp)
                        ) {
                            Text(
                                text = item,
                                color = MaterialTheme.colors.error,
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontWeight = FontWeight.Bold),
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
            item {
                Text(
                    text = "Expense Categories",
                    color = MaterialTheme.colors.onBackground,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp, 0.dp)
                )
            }

            val rowCap = (screenWeight / 76)
            val rowNumber = if ((gridItems.size % rowCap) == 0) {
                gridItems.size / rowCap
            } else {
                (gridItems.size / rowCap) + 1
            }
            (1..rowNumber).forEach {
                item {
                    ItemRow(
                        gridItems.subList(
                            (rowCap * (it - 1)),
                            if ((rowCap * (it - 1)) + rowCap > gridItems.size) {
                                gridItems.size
                            } else {
                                (rowCap * (it - 1)) + rowCap
                            }
                        )
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(88.dp)) }
        }
    }
}
