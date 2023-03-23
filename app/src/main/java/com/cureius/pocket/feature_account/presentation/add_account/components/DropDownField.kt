package com.cureius.pocket.feature_account.presentation.add_account.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize

@Composable
fun DropDownField() {
    var mExpanded by remember { mutableStateOf(false) }
    val mCities = listOf("Delhi", "Mumbai", "Chennai", "Kolkata", "Hyderabad", "Bengaluru", "Pune")
    var mSelectedText by remember { mutableStateOf("") }
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }
    val icon = if (mExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(Modifier.padding(20.dp)) {
        OutlinedTextField(
            value = mSelectedText,
            onValueChange = { mSelectedText = it },
            modifier = Modifier
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    mTextFieldSize = coordinates.size.toSize()
                },
            label = { Text("Bank") },
            trailingIcon = {
                Icon(icon, "contentDescription",
                    Modifier.clickable { mExpanded = !mExpanded })
            }
        )
        DropdownMenu(
            expanded = mExpanded,
            onDismissRequest = { mExpanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { mTextFieldSize.width.toDp() })
        ) {
            mCities.forEach { label ->
                DropdownMenuItem(
                    onClick = {
                        mSelectedText = label
                        mExpanded = false
                    }, modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxSize()
                ) {
                    Text(
                        text = label,
                        modifier = Modifier
                            .background(MaterialTheme.colors.background)
                            .fillMaxSize()
                    )
                }
            }
        }
    }
}
