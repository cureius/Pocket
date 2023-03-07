package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cureius.pocket.R

@Composable
fun DashBoardHeader(navHostController: NavHostController) {
    Row(
        modifier = Modifier
            .height(112.dp)
            .padding(28.dp, 16.dp, 28.dp, 0.dp)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        val profile = ImageVector.vectorResource(id =R.drawable.user)
        val wallet = ImageVector.vectorResource(id =R.drawable.wallet)
        val activity = ImageVector.vectorResource(id =R.drawable.notification)

        RoundIconButton(icon = profile, label = "Profile", onClick = {
            navHostController.navigate("profile")
        })

        Row() {

            RoundIconButton(icon = wallet, label = "Wallet", onClick = {
                navHostController.navigate("wallet")
            })

            RoundIconButton(icon = activity, label = "Activity", onClick = {
                navHostController.navigate("activity")
            })
        }

    }
}
