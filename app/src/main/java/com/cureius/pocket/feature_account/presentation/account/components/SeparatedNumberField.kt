package com.cureius.pocket.feature_account.presentation.account.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun SeparatedNumberField(
    modifier: Modifier = Modifier,
    digitText: String,
    digitCount: Int = 3,
    onNumberChange: (String, Boolean) -> Unit
) {
    BasicTextField(
        modifier = modifier,
        value = digitText,
        onValueChange = {
            if (it.length <= digitCount) {
                onNumberChange.invoke(it, it.length == digitCount)
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(horizontalArrangement = Arrangement.Center) {
                repeat(digitCount) { index ->
                    CharBoxView(
                        index = index,
                        text = digitText
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
}