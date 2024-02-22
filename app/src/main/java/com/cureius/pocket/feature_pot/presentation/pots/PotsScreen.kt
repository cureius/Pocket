package com.cureius.pocket.feature_pot.presentation.pots

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cureius.pocket.R
import com.cureius.pocket.feature_pot.presentation.add_pot.AddPotEvent
import com.cureius.pocket.feature_pot.presentation.add_pot.AddPotViewModel
import com.cureius.pocket.feature_pot.presentation.add_pot.cmponents.AddPotDialog
import com.cureius.pocket.feature_pot.presentation.pots.components.AddPotCard
import com.cureius.pocket.feature_pot.presentation.pots.components.PotItem
import com.cureius.pocket.feature_transaction.presentation.transactions.TransactionsViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PotsScreen(
    navHostController: NavHostController,
    viewModel: PotsViewModel = hiltViewModel(),
    transactionViewModel: TransactionsViewModel = hiltViewModel(),
    addPotViewModel: AddPotViewModel = hiltViewModel()
) {
    val monthlyPots = viewModel.validPots.value
    val templatePots = viewModel.templatePots.value

    Scaffold {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
        ) {
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp)
                        .height(60.dp)
                ) {
                    Text(
                        text = "Pots",
                        color = MaterialTheme.colors.onBackground,
                        textAlign = TextAlign.Center,
                        style = TextStyle(fontWeight = FontWeight.Bold),
                        fontSize = 24.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(4.dp, 0.dp)
                    )
                        Box(
                            modifier = Modifier
                                .background(
                                    color = MaterialTheme.colors.primary.copy(
                                        alpha = 0.4f
                                    ), RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp)
                                .clickable {
                                    navHostController.navigate("configure_pots")
                                }, contentAlignment = Alignment.Center
                        ) {
                            val config = ImageVector.vectorResource(id = R.drawable.sliders)
                            Icon(
                                imageVector = config,
                                contentDescription = "config",
                                tint = MaterialTheme.colors.onBackground,
                            )
                        }

                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            for (pot in templatePots) {
                item {
                    PotItem(pot)
                }
            }
            if (templatePots.isEmpty()) {
                item {
                    AddPotCard(addPotViewModel)
                }
            }
            item {
                Spacer(modifier = Modifier.height(140.dp))
            }
        }
        if (addPotViewModel.dialogVisibility.value) {
            AddPotDialog(onDismiss = {
                addPotViewModel.onEvent(AddPotEvent.ToggleAddAccountDialog)
            }, onSubmit = {

            })
        }
    }
}
