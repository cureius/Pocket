package com.cureius.pocket.feature_account.presentation.account.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cureius.pocket.R
import com.cureius.pocket.feature_transaction.domain.model.Transaction

@Composable
fun AccountItem (){
    val paddingModifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .height(160.dp)
    Card(
        shape = RoundedCornerShape(20.dp),
        elevation = 4.dp,
        modifier = paddingModifier,
    ) {
        Box(
            modifier = Modifier
                .background(Transaction.transactionColors.random())
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Column() {
                Row {
                    val borderWidth = 2.dp
                    Image(
                        painter = painterResource(id = R.drawable.accounts),
                        contentDescription = "Bank Logo",
                        contentScale = ContentScale.Inside,
                        modifier = Modifier
                            .size(36.dp)
                            .border(
                                BorderStroke(borderWidth, MaterialTheme.colors.primary),
                                CircleShape
                            )
                            .padding(borderWidth)
                            .clip(CircleShape),
                        colorFilter = ColorFilter.tint(MaterialTheme.colors.primary)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Column() {
                        Text(
                            text = "United Bank Of India",
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(4.dp, 0.dp)
                        )
                        Text(
                            text = "Bank Account",
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            fontSize = 8.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(4.dp, 0.dp)
                        )
                    }

                }
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val pngImage: Painter = painterResource(id = R.drawable.chip)
                    Image(
                        painter = pngImage,
                        contentDescription = "smart chip",
                        modifier = Modifier
                            .size(72.dp),
                    )
                    Column {
                        Text(
                            text = "XXXX XXXX XXXX 0286",
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(4.dp, 0.dp)
                        )
                        Text(
                            text = "**********755",
                            color = Color.Black,
                            textAlign = TextAlign.Center,
                            style = TextStyle(fontWeight = FontWeight.Bold),
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(4.dp, 0.dp)
                        )
                    }
                }
                Text(
                    text = "SOURAJ PAL",
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(4.dp, 0.dp)
                )
            }
        }
    }
}