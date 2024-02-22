package com.cureius.pocket.feature_transaction.presentation.transactions.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.cureius.pocket.feature_pot.domain.util.IconDictionary
import com.cureius.pocket.feature_transaction.domain.model.Transaction
import java.time.LocalDate

@Composable
fun TransactionItem(
    transaction: Transaction,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 50.dp,
    cutCornerSize: Dp = 0.dp,
    showDate: Boolean = true,
    onDeleteClick: () -> Unit,
) {
    Column {
        transaction.date?.let {
            if (showDate) {
                println("Date Long: ${it}")
//                println("Date: ${LocalDate.ofEpochDay(it)}")

//                Text(
//                    text = LocalDate.ofEpochDay(it)
//                        .format(java.time.format.DateTimeFormatter.ofPattern("dd MMM yyyy")),
//                    style = MaterialTheme.typography.body1,
//                    color = Color.Gray,
//                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
        Box(modifier = modifier) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val clipPath = Path().apply {
                    lineTo(size.width - cutCornerSize.toPx(), 0f)
                    lineTo(size.width, cutCornerSize.toPx())
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                }

                clipPath(clipPath) {
//                    transaction?.color?.let { Color(it) }?.let {
//                        drawRoundRect(
//                            color = it,
//                            size = size,
//                            cornerRadius = CornerRadius(cornerRadius.toPx())
//                        )
//                    }
//                    transaction.color?.let { ColorUtils.blendARGB(it, 0x000000, 0.2f) }?.let {
//                        Color(
//                            it
//                        )
//                    }?.let {
//                        drawRoundRect(
//                            color = it,
//                            topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
//                            size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
//                            cornerRadius = CornerRadius(cornerRadius.toPx())
//                        )
//                    }
                    drawRoundRect(
                        color = Color.Transparent,
                        size = size,
                        cornerRadius = CornerRadius(cornerRadius.toPx())
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.1f),
                        shape = RoundedCornerShape(50.dp)
                    )
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically

            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier
                            .background(
                                color = if (transaction.type
                                        ?.lowercase()
                                        .equals("credited", true)
                                ) {
                                    Color.Green.copy(alpha = 0.7f)
                                } else {
                                    Color.Red.copy(alpha = 0.6f)
                                },
                                CircleShape
                            )
                            .padding(8.dp)
                    ) {
                        Image(
                            painter = painterResource(id = IconDictionary.allIcons["outGoingArrow"]!!),
                            contentDescription = "outGoingArrow",
                            modifier = Modifier
                                .size(20.dp)
                                .rotate(
                                    degrees = if (transaction.type
                                            ?.lowercase()
                                            .equals("credited", true)
                                    ) {
                                        180f
                                    } else {
                                        0f
                                    }
                                ),
                            colorFilter =
                            ColorFilter.tint(
                                MaterialTheme.colors.surface
                            )

                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        if (transaction.title != null)
                            Text(
                                text = transaction.title.toString(),
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        Text(
                            text = if (transaction.type?.lowercase().equals("credited", true)) {
                                "+" + transaction.amount.toString()
                            } else {
                                "-" + transaction.amount.toString()
                            },
                            style = MaterialTheme.typography.body1,
                            color = if (transaction.type?.lowercase().equals("credited", true)) {
                                MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            } else {
                                MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                            },
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                transaction.body.let {
                    if (it != null) {
                        if (it.isNotEmpty()) {
                            Text(
                                text = transaction.body.toString().split(" ")
                                    .joinToString(" ") { it.capitalize() },
                                style = MaterialTheme.typography.body2,
                                color = MaterialTheme.colors.onSurface.copy(alpha = 0.4f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.fillMaxWidth(0.75f)
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(
                    onClick = { onDeleteClick() },
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete Transaction",
                        tint = MaterialTheme.colors.onSurface
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun IncomeTransactionItemPreview() {
    TransactionItem(
        transaction = Transaction(
            title = "Salary",
            type = "credited",
            amount = 100.0,
            body = "This is a transaction",
            color = Color.White.toArgb(),
            timestamp = System.currentTimeMillis()
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {}
}


@Preview
@Composable
fun SpendingTransactionItemPreview() {
    TransactionItem(
        transaction = Transaction(
            title = "Food",
            type = "debited",
            amount = 100.0,
            body = null,
            color = Color.White.toArgb(),
            timestamp = System.currentTimeMillis(),
            date = LocalDate.now().toEpochDay()
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {}
}