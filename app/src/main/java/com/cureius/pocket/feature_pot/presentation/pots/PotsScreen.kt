package com.cureius.pocket.feature_pot.presentation.pots

import android.widget.Space
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
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
import com.cureius.pocket.feature_pot.domain.model.Pot
import com.cureius.pocket.feature_pot.presentation.add_pot.AddPotEvent
import com.cureius.pocket.feature_pot.presentation.add_pot.AddPotViewModel
import com.cureius.pocket.feature_pot.presentation.add_pot.cmponents.AddPotDialog
import com.cureius.pocket.feature_pot.presentation.pots.components.AddPotCard
import com.cureius.pocket.feature_pot.presentation.pots.components.PotItem

@Composable
fun PotsScreen(
    navHostController: NavHostController,
    viewModel: PotsViewModel = hiltViewModel(),
    addPotViewModel: AddPotViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val pots = mutableListOf<Pot>()
    pots.clear()
    state.filter { it.is_template == true }.forEach { template ->
        if (state.any { it.parent == template.id }) {
            pots.add(state.last { it.parent == template.id })
        } else {
            pots.add(template)
        }
    }
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
                                .background(
                                    color = MaterialTheme.colors.primary.copy(
                                        alpha = 0.4f
                                    )
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
                                tint = MaterialTheme.colors.background,
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
            items(pots) { pot ->
                PotItem(pot)
            }
            item {
                AddPotCard(addPotViewModel)
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
