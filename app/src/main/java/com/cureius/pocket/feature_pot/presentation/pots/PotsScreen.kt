package com.cureius.pocket.feature_pot.presentation.pots

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.cureius.pocket.R
import com.cureius.pocket.feature_pot.presentation.add_pot.cmponents.AddPotDialog
import com.cureius.pocket.feature_pot.presentation.pots.components.AddPotCard
import com.cureius.pocket.feature_pot.presentation.pots.components.PotItem

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
                pot.icon?.let {
                    PotItem(
                        pot.title,
                        it,
                        pot.capacity,
                        pot.filled
                    )
                }
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
