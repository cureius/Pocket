package com.cureius.pocket.feature_transaction.presentation.transactions.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.graphics.ColorUtils
import com.cureius.pocket.feature_transaction.domain.model.Transaction

@Composable
fun TransactionItem(
    transaction: Transaction,
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 10.dp,
    cutCornerSize: Dp = 30.dp,
    onDeleteClick: () -> Unit
) {
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
                transaction?.color?.let { Color(it) }?.let {
                    drawRoundRect(
                        color = it,
                        size = size,
                        cornerRadius = CornerRadius(cornerRadius.toPx())
                    )
                }
                transaction.color?.let { ColorUtils.blendARGB(it, 0x000000, 0.2f) }?.let {
                    Color(
                        it
                    )
                }?.let {
                    drawRoundRect(
                        color = it,
                        topLeft = Offset(size.width - cutCornerSize.toPx(), -100f),
                        size = Size(cutCornerSize.toPx() + 100f, cutCornerSize.toPx() + 100f),
                        cornerRadius = CornerRadius(cornerRadius.toPx())
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .padding(end = 32.dp)
        ) {
            transaction.type?.uppercase()?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.h6,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = transaction.amount.toString(),
                style = MaterialTheme.typography.body1,
                color = Color.Blue,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = transaction.body.toString(),
                style = MaterialTheme.typography.body1,
                color = Color.Gray,
                maxLines = 10,
                overflow = TextOverflow.Ellipsis
            )
        }
        IconButton(onClick = { onDeleteClick() },
        modifier = Modifier.align(Alignment.BottomEnd)) {
            Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Transaction")
        }

    }
}

@Preview
@Composable
fun TransactionItemPreview(){
    TransactionItem(
        transaction = Transaction(
            title = "Transaction",
            type = "Income",
            amount = 100.0,
            body = "This is a transaction",
            color = Color.White.toArgb(),
            timestamp = System.currentTimeMillis()
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .padding(16.dp)
    ){}
}