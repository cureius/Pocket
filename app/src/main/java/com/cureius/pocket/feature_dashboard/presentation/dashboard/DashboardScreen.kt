package com.cureius.pocket.feature_dashboard.presentation.dashboard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.cureius.pocket.feature_dashboard.domain.PotType
import com.cureius.pocket.feature_dashboard.presentation.dashboard.components.*
import com.cureius.pocket.feature_pot.presentation.pots.components.CategoryItem
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.domain.util.IconDictionary
import com.cureius.pocket.util.ScreenDimensions

@Composable
fun DashboardScreen(navHostController: NavHostController) {
    val save = ImageVector.vectorResource(id = R.drawable.save)
    val wallet = ImageVector.vectorResource(id = R.drawable.wallet)
    val emi = ImageVector.vectorResource(id = R.drawable.coins)
    val invest = ImageVector.vectorResource(id = R.drawable.shop)
    val add = ImageVector.vectorResource(id = R.drawable.add)

    val food = ImageVector.vectorResource(id = R.drawable.food)
    val entertainment = ImageVector.vectorResource(id = R.drawable.food)
    val travel = ImageVector.vectorResource(id = R.drawable.travel)
    val house = ImageVector.vectorResource(id = R.drawable.home)
    val fuel = ImageVector.vectorResource(id = R.drawable.fuel)
    val health = ImageVector.vectorResource(id = R.drawable.health)
    val shopping = ImageVector.vectorResource(id = R.drawable.shopping)
    val grocery = ImageVector.vectorResource(id = R.drawable.shop)

    val gridItems = listOf(
        CategoryItem(
            icon = food,
            name = "Food",
            fill = 0.4,
        ),
        CategoryItem(
            icon = entertainment,
            name = "Fun",
            fill = 0.7,
        ),
        CategoryItem(
            icon = travel,
            name = "Travel",
            fill = 0.2,
        ),
        CategoryItem(
            icon = house,
            name = "House",
            fill = 0.8,
        ),
        CategoryItem(
            icon = fuel,
            name = "Fuel",
            fill = 0.3,
        ),
        CategoryItem(
            icon = health,
            name = "Health",
            fill = 0.4,
        ),
        CategoryItem(
            icon = shopping,
            name = "Shopping",
            fill = 1.0,
        ),
        CategoryItem(
            icon = grocery,
            name = "Grocery",
            fill = 0.8,
        ),
        CategoryItem(
            adder = true
        ),
    )
    val potItems = listOf(
        Pot(
            icon = "save",
            title = "Savings",
            capacity = 100.0,
            filled = 0.8f,
            type = PotType.Asset.type
        ),
        Pot(
            icon = "wallet",
            title = "Wallet",
            capacity = 100.0,
            filled = 0.5f,
            type = PotType.Liability.type
        ),
        Pot(
            icon = "emi",
            title = "EMI",
            capacity = 100.0,
            filled = 0.6f,
            type = PotType.Liability.type
        ),
        Pot(
            icon = "invest",
            title = "Investment",
            capacity = 100.0,
            filled = 0.7f,
            type = PotType.Asset.type
        )
    )
    val size = ScreenDimensions()
    val screenWeight = size.width()
    val rowCap = (screenWeight / 76)
    val rowNumber = if ((gridItems.size % rowCap) == 0) {
        gridItems.size / rowCap
    } else {
        (gridItems.size / rowCap) + 1
    }
    val startPadding = ((screenWeight - (rowCap * 80)) / 2)
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
                .offset(y = (-26).dp),
            contentPadding = PaddingValues(vertical = 12.dp, horizontal = 16.dp)
        ) {
            item {
                AddAccountRequest(position = -1)
            }
            item {
                AddPotRequest(position = 0)
            }
            item {
                SyncSMS(position = 0)
            }
            item {
                IncomeCreditPrompt(position = 1)
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
                        .height(152.dp),
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
                    Log.d("Pots", "DashboardScreen: $potItems")
                    items(potItems) { item ->
                        item.filled?.let { item.type?.let { it1 ->
                            PotItem(item.icon, it, item.title,
                                it1
                            )
                        } }
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
            item { Spacer(modifier = Modifier.height(16.dp)) }
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
                        ),
                        modifier = Modifier.padding(startPadding.dp)
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(88.dp)) }
        }
    }
}
