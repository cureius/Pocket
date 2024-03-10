package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.cureius.pocket.R
import com.cureius.pocket.feature_dashboard.presentation.dashboard.DashBoardEvent
import com.cureius.pocket.feature_dashboard.presentation.dashboard.DashBoardViewModel
import com.cureius.pocket.feature_sms_sync.presentation.SmsService
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.isGranted

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun DashBoardHeader(navHostController: NavHostController, viewModel: DashBoardViewModel, permissionState: MultiplePermissionsState) {
    Row(
        modifier = Modifier
            .height(82.dp)
            .padding(0.dp, 16.dp, 20.dp, 0.dp)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        val context: Context = LocalContext.current
        val profile = ImageVector.vectorResource(id = R.drawable.user)
        val logo = ImageVector.vectorResource(R.drawable.logo)
        val wallet = ImageVector.vectorResource(id = R.drawable.wallet)
        val sync = ImageVector.vectorResource(R.drawable.download)
        val activity = ImageVector.vectorResource(id = R.drawable.notification)
        val search = ImageVector.vectorResource(R.drawable.outline_search_24)
        val sairaCondensedFontFamily = FontFamily(
            Font(R.font.saira_medium, FontWeight.Normal, FontStyle.Normal)
        )
        val intent = Intent(context, SmsService::class.java)

        Row {
            Box(
                modifier = Modifier
                    .background(
                        MaterialTheme.colors.primary.copy(alpha = 0.0F), CircleShape
                    )
                    .height(60.dp)
                    .width(60.dp),
                contentAlignment = Alignment.Center
            ) {
                IconButton(onClick = {

                }) {
                    Icon(
                        imageVector = logo,
                        contentDescription = "logo",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
//            RoundIconButton(icon = logo, iconColor = MaterialTheme.colors.primary, label = "", modifier = Modifier
//                .background(
//                    MaterialTheme.colors.primary.copy(alpha = 0.0F), CircleShape
//                )
//                .height(60.dp)
//                .width(60.dp), onClick = {
////                navHostController.navigate("profile")
//            })
            Text(
                text = "Pocket",
                fontFamily = sairaCondensedFontFamily,
                style = MaterialTheme.typography.h4,
            )

        }

        Row() {
            RoundIconButton(icon = search, label = "", modifier = Modifier
                .background(
                    MaterialTheme.colors.primary.copy(alpha = 0.2F), CircleShape
                )
                .height(40.dp)
                .width(40.dp), onClick = {
                navHostController.navigate("activity")
            })
            RoundIconButton(icon = sync, label = "", modifier = Modifier
                .background(
                    MaterialTheme.colors.primary.copy(alpha = 0.2F), CircleShape
                )
                .height(40.dp)
                .width(40.dp), onClick = {
                Toast.makeText(context, "SMS Sync Started", Toast.LENGTH_LONG).show()

                viewModel.onEvent(DashBoardEvent.ToggleAskPermission)
                context.startService(intent)

                var checkAllPermission = true;
                permissionState.permissions.forEach {
                    if (!it.status.isGranted) {
                        checkAllPermission = false
                    }
                }
                if (checkAllPermission) {
                    println("Permission Granted")
                    viewModel.onEvent(DashBoardEvent.ReadAllSMS)
                }
                Toast.makeText(context, "SMS Sync Done", Toast.LENGTH_SHORT).show()
            })
        }

    }
}
