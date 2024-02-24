package com.cureius.pocket.feature_dashboard.presentation.dashboard.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.cureius.pocket.feature_dashboard.domain.PotType
import com.cureius.pocket.feature_pot.domain.util.IconDictionary
import com.cureius.pocket.feature_transaction.presentation.transactions.TransactionsViewModel
import com.cureius.pocket.feature_pot.domain.model.Pot


@Composable
fun PotItem(
    pot : Pot,
    icon: String?,
    filled: Float? = 0.0f,
    name: String? = "",
    potType: String? = "",
    transactionsViewModel: TransactionsViewModel = hiltViewModel(),
) {
    val potShape = RoundedCornerShape(
        topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp
    )
    val fillShape = RoundedCornerShape(
        topStart = 12.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 12.dp
    )
    val transactions = transactionsViewModel.state.value.transactionsOnCurrentMonthForAccounts.filter { it.pot == pot.title }
    val totalAmount = transactions.sumOf { it.amount!! }
    val totalCapacity =
        transactionsViewModel.state.value.transactionsOnCurrentMonthForAccounts.filter { it.type == "credited" }
            .sumOf { it.amount!! }
    var actualCapacity = 0.0;
    var filled = 0.0f;
    if (pot.weight != null) {
        actualCapacity = totalCapacity * pot.weight!!
        if (actualCapacity.toFloat() != 0.0f) {
            filled = totalAmount.toFloat() / actualCapacity.toFloat();
        }
    }
    Row(modifier = Modifier.background(MaterialTheme.colors.background)) {
        Column() {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .width(76.dp)
                        .height(100.dp)
                        .background(MaterialTheme.colors.surface, potShape),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    (if (filled!! in 0.0..1.0) {
                        filled
                    } else {
                        1f
                    }).let {
                        Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(
                                fraction = it
                            )
                            .background(
                                color = when (potType) {
                                    PotType.Asset.type -> {
                                        MaterialTheme.colors.secondary
                                    }

                                    PotType.Liability.type -> {
                                        MaterialTheme.colors.error
                                    }

                                    else -> {
                                        MaterialTheme.colors.primary.copy(alpha = 0.9f)
                                    }
                                }, fillShape
                            )
                    }.let {
                        Box(
                            modifier = it, contentAlignment = Alignment.Center
                        ) {

                        }
                    }

                }
                if (icon != null) {
                    IconDictionary.allIcons[icon]
                        ?.let { ImageVector.vectorResource(id = it) }?.let {
                            Icon(
                                imageVector = it,
                                contentDescription = name,
                                modifier = Modifier.size(40.dp),
                                tint = MaterialTheme.colors.background
                            )
                        }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            if (name != null) {
                Text(
                    text = name,
                    color = when (potType) {
                        PotType.Asset.type -> MaterialTheme.colors.secondary
                        PotType.Asset.type -> MaterialTheme.colors.error
                        else -> {
                            MaterialTheme.colors.onSurface
                        }
                    },
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Normal),
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .padding(4.dp, 0.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Composable
@Preview
fun PotItemPreview() {
    PotItem(
        pot = Pot(
            title = "Savings",
            weight = 0.0f,
            icon = "save",
            type = PotType.Asset.type
        ),
        icon = "save",
        filled = 1.0f,
        name = "Savings",
        potType = PotType.Asset.type
    )
}